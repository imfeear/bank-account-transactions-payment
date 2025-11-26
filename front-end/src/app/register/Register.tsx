"use client";

import { useState, FormEvent } from "react";
import { useRouter } from "next/navigation";
import { Eye, EyeOff } from "lucide-react";
import Image from "next/image";
import logoImage from "@/images/bank.png";
// Interface para os dados da conta retornados pela API
interface AccountData {
  accountNumber: string;
  agencyNumber: string;
}


// Estado inicial do formulário
const initialFormState = {
  personType: "",
  password: "",
  confirmPassword: "",
  email: "",
  phone_number: "",
  monthly_income: 0,
  adress: {
    street: "",
    neighborhood: "",
    cep: "",
    number: "",
    state: "",
    city: "",
    complement: "",
  },
  naturalPerson: { full_name: "", cpf: "", born_date: "" },
  legalEntity: { razao_social: "", cnpj: "", responsible: "" },
};

const Register = () => {
  const [formData, setFormData] = useState(initialFormState);
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [errorMessage, setErrorMessage] = useState("");
  const [accountData, setAccountData] = useState<AccountData | null>(null);
  const router = useRouter();
  const [isAddressLocked, setIsAddressLocked] = useState(false);


  const handleChange = (
      e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;

    setFormData((prev) => {
      const updatedFormData = { ...prev };
      const keys = name.split(".");


      let currentLevel = updatedFormData as any;

      keys.forEach((key, index) => {
        if (index === keys.length - 1) {
          currentLevel[key] = value;
        } else {
          if (!currentLevel[key]) currentLevel[key] = {};
          currentLevel = currentLevel[key];
        }
      });

      return updatedFormData;
    });

    if (errors[name]) {
      setErrors((prevErrors) => {
        const updatedErrors = { ...prevErrors };
        delete updatedErrors[name];
        return updatedErrors;
      });
    }
  };

  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();

    // Impede múltiplas submissões enquanto uma requisição está em andamento
    if (isSubmitting) return;
    setIsSubmitting(true); // Define como verdadeiro durante a submissão

    const newErrors: Record<string, string> = {};

    // Validação dos campos do formulário
    if (!formData.personType) newErrors.personType = "Selecione um tipo de cliente.";
    if (formData.personType === "fisica" && !formData.naturalPerson.cpf) {
      newErrors["naturalPerson.cpf"] = "CPF é obrigatório.";
    }
    if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = "As senhas não coincidem.";
    }

    setErrors(newErrors);

    if (Object.keys(newErrors).length > 0) {
      setIsSubmitting(false); // Libera a submissão caso haja erros de validação
      return;
    }

    try {
      const response = await fetch("http://localhost:8081/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });

      const data = await response.json();

      if (!response.ok) {
        setErrors({ general: data.message || "Erro ao registrar usuário." });
      } else {
        setAccountData({
          accountNumber: data.accountNumber,
          agencyNumber: data.agencyNumber,
        });
      }
    } catch (err) {
      console.error("Erro durante o registro:", err);
      setErrors({ general: "Erro inesperado." });
    } finally {
      setIsSubmitting(false); // Libera a submissão após o processamento da requisição
    }
  };



  const fetchAddress = async (cep: string) => {
    try {
      const response = await fetch(`https://viacep.com.br/ws/${cep}/json/`);
      const data = await response.json();
  
      if (data.erro) {
        setErrors({ "adress.cep": "CEP não encontrado." });
        return;
      }
  
      setFormData((prev) => ({
        ...prev,
        adress: {
          ...prev.adress,
          street: data.logradouro || "",
          neighborhood: data.bairro || "",
          state: data.uf || "",
          city: data.localidade || "",
          complement: data.complemento || "",
        },
      }));
  
      // Bloqueia os campos após preenchimento automático
      setIsAddressLocked(true);
    } catch (err) {
      console.error("Erro ao buscar endereço:", err);
      setErrors({ "adress.cep": "Erro ao buscar endereço." });
    }
  };

  const handleCepChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    handleChange(e);
    const cep = e.target.value;
  
    if (cep.length < 8) {
      setIsAddressLocked(false); // Desbloqueia os campos ao alterar o CEP
    }
  
    if (cep.length === 8) {
      fetchAddress(cep);
    }
  };
  


  const togglePasswordVisibility = () => setShowPassword(!showPassword);
  const toggleConfirmPasswordVisibility = () =>
      setShowConfirmPassword(!showConfirmPassword);

  if (accountData) {
    return (
        <div className="flex items-center justify-center min-h-screen">
          <div className="bg-white p-8 rounded shadow-lg text-center">
            <h2 className="text-2xl font-bold text-blue-600">Conta Criada com Sucesso!</h2>
            <p className="mt-4"><strong>Número da Conta:</strong> {accountData.accountNumber}</p>
            <p><strong>Número da Agência:</strong> {accountData.agencyNumber}</p>
            <button
                onClick={() => router.push("/login")}
                className="mt-6 bg-blue-600 text-white py-2 px-4 rounded hover:bg-blue-700"
            >
              Ir para Login
            </button>
          </div>
        </div>
    );
  }

  return (
      <section className="flex flex-col min-h-screen bg-white">
        <header className="bg-[#708090] text-white h-16 flex items-center px-6">
          <div
              className="flex items-center cursor-pointer"
              style={{ marginLeft: "22px" }}
              onClick={() => router.push("/login")}
          >
            <Image src={logoImage} alt="Logo do New Bank" width={50} height={50} />
          </div>
          <div className="flex-grow flex justify-center">
            <nav className="flex space-x-10">
              <a href="#" className="hover:underline">Serviços</a>
              <a href="#" className="hover:underline">Ajuda</a>
              <a href="#" className="hover:underline">Sobre</a>
            </nav>
          </div>
        </header>


        <main className="flex justify-start items-start p-12 flex-grow">
          <form className="grid grid-cols-2 gap-1.5 w-full lg:w-2/5" onSubmit={handleSubmit}>
            {errors.general && <div className="text-red-500 col-span-2">{errors.general}</div>}

            {/* Seletor de tipo de pessoa */}
            <div className="col-span-2 font-bold text-md">
              Selecione o tipo de cliente
            </div>
            <label className="col-span-2 block">
              <select
                  name="personType"
                  value={formData.personType}
                  onChange={handleChange}
                  className="w-full p-1 border rounded text-gray-400"
                  required
              >
                <option value="">Selecione...</option>
                <option value="fisica">Pessoa Física</option>
                <option value="juridica">Pessoa Jurídica</option>
              </select>
            </label>

            {formData.personType && (
                <>
                  {/* Campos para Pessoa Física */}
                  {formData.personType === "fisica" && (
                      <>
                        <div className="col-span-2 font-bold text-md">
                          Informações Pessoais
                        </div>
                        <div className="flex space-x-4 mb-4">
                          <label className="block w-1/2">
                            <input
                                type="text"
                                name="naturalPerson.full_name"
                                placeholder="Nome Completo"
                                value={formData.naturalPerson.full_name}
                                onChange={handleChange}
                                className="p-1 border rounded w-full"
                                required
                            />
                          </label>

                          <label className="block w-1/2">
                            <input
                                type="text"
                                name="naturalPerson.cpf"
                                placeholder="CPF"
                                value={formData.naturalPerson.cpf}
                                onChange={handleChange}
                                className={`p-1 border rounded w-full ${errors["naturalPerson.cpf"] ? "border-red-500" : ""}`}
                                maxLength={11}
                                required
                            />
                            {errors["naturalPerson.cpf"] && <span className="text-red-500 text-sm">{errors["naturalPerson.cpf"]}</span>}
                          </label>
                        </div>

                        <div className="flex space-x-4 mb-4">
                          <label className="block w-1/2">
                            <input
                                type="date"
                                name="naturalPerson.born_date"
                                value={formData.naturalPerson.born_date}
                                onChange={handleChange}
                                className="p-1 border rounded w-full text-gray-500"
                            />
                          </label>

                          <label className="block">
                            <input
                              type="number"
                              name="monthly_income"
                              placeholder="Renda Mensal"
                              value={formData.monthly_income || ""} // Garante que o valor padrão seja vazio
                              onChange={handleChange}
                              className="w-full p-1 border rounded"
                              required
                            />
                          </label>
                        </div>

                      </>
                  )}

                  {/* Campos para Pessoa Jurídica */}
                  {formData.personType === "juridica" && (
                      <>
                        <div className="col-span-2 font-bold text-md">
                          Informações da Empresa
                        </div>
                        <div className="grid grid-cols-2 gap-4 mb-4">
                          <label className="block">
                            <input
                                type="text"
                                name="legalEntity.razao_social"
                                placeholder="Razão Social"
                                value={formData.legalEntity.razao_social}
                                onChange={handleChange}
                                className="w-full p-1 border rounded"
                                required
                            />
                          </label>

                          <label className="block">
                            <input
                                type="text"
                                name="legalEntity.cnpj"
                                placeholder="CNPJ"
                                value={formData.legalEntity.cnpj}
                                onChange={handleChange}
                                className="w-full p-1 border rounded"
                                maxLength={14}
                                required
                            />
                          </label>
                        </div>

                        <div className="grid grid-cols-2 gap-4 mb-4">
                          <label className="block">
                            <input
                                type="text"
                                name="legalEntity.responsible"
                                placeholder="Nome do Responsável"
                                value={formData.legalEntity.responsible}
                                onChange={handleChange}
                                className="w-full p-1 border rounded pr-12"
                                required
                            />
                          </label>

                          <label className="block">
                            <input
                              type="number"
                              name="monthly_income"
                              placeholder="Renda Mensal"
                              value={formData.monthly_income || ""} // Garante que o valor padrão seja vazio
                              onChange={handleChange}
                              className="w-full p-1 border rounded"
                              required
                            />
                          </label>
                        </div>
                      </>
                  )}

                  {/* Endereço */}
                  <div className="col-span-2 font-bold text-md">Endereço</div>

                  <div className="grid grid-cols-2 gap-4 mb-4">
                        <label className="block">
                          <input
                            type="text"
                            name="adress.street"
                            placeholder="Rua"
                            value={formData.adress.street}
                            onChange={handleChange}
                            className={`w-full p-1 border rounded ${isAddressLocked ? "bg-gray-200 cursor-not-allowed" : ""}`}
                            readOnly={isAddressLocked}
                            required
                          />
                        </label>

                        <label className="block">
                          <input
                            type="text"
                            name="adress.neighborhood"
                            placeholder="Bairro"
                            value={formData.adress.neighborhood}
                            onChange={handleChange}
                            className={`w-full p-1 border rounded ${isAddressLocked ? "bg-gray-200 cursor-not-allowed" : ""}`}
                            readOnly={isAddressLocked}
                            required
                          />
                        </label>
                      </div>

                      <div className="grid grid-cols-2 gap-4 mb-4">
                          <label className="block">
                            <input
                              type="text"
                              name="adress.cep"
                              placeholder="CEP"
                              value={formData.adress.cep}
                              onChange={(e) => {
                                handleChange(e);
                                const cep = e.target.value;

                                if (cep.length < 8) {
                                  setIsAddressLocked(false); // Desbloqueia os campos se o CEP for alterado
                                }

                                if (cep.length === 8) {
                                  fetchAddress(cep); // Preenche automaticamente o endereço
                                }
                              }}
                              className={`w-full p-1 border rounded ${
                                errors["adress.cep"] ? "border-red-500" : ""
                              }`}
                              maxLength={8}
                              required
                            />
                            {errors["adress.cep"] && <span className="text-red-500">{errors["adress.cep"]}</span>}
                          </label>

                    <label className="block">
                      <input
                          type="text"
                          name="adress.number"
                          placeholder="Número"
                          value={formData.adress.number}
                          onChange={handleChange}
                          className="w-full p-1 border rounded"
                          required
                      />
                    </label>
                  </div>

                  <div className="grid grid-cols-2 gap-4 mb-4">
                      <label className="block">
                        <input
                          type="text"
                          name="adress.state"
                          placeholder="Estado"
                          value={formData.adress.state}
                          onChange={handleChange}
                          className={`w-full p-1 border rounded ${isAddressLocked ? "bg-gray-200 cursor-not-allowed" : ""}`}
                          readOnly={isAddressLocked}
                          required
                        />
                      </label>

                      <label className="block">
                        <input
                          type="text"
                          name="adress.city"
                          placeholder="Cidade"
                          value={formData.adress.city}
                          onChange={handleChange}
                          className={`w-full p-1 border rounded ${isAddressLocked ? "bg-gray-200 cursor-not-allowed" : ""}`}
                          readOnly={isAddressLocked}
                          required
                        />
                      </label>
                    </div>

                  <label className="col-span-2 block">
                    <input
                        type="text"
                        name="adress.complement"
                        placeholder="Complemento"
                        value={formData.adress.complement}
                        onChange={handleChange}
                        className="w-full p-1 border rounded pr-12"
                    />
                  </label>

                  {/* Contato e Senha */}
                  <div className="col-span-2 font-bold text-md">
                    Contato e Acesso
                  </div>

                  <label className="col-span-1 block">
                    <input
                        type="text"
                        name="phone_number"
                        placeholder="Telefone"
                        value={formData.phone_number}
                        onChange={handleChange}
                        className="w-full p-1 border rounded pr-12"
                        required
                    />
                  </label>

                  <label className="col-span-1 block">
                    <input
                        type="email"
                        name="email"
                        placeholder="E-mail"
                        value={formData.email}
                        onChange={handleChange}
                        className="w-full p-1 border rounded pr-12"
                        required
                    />
                  </label>
                  <label className="col-span-1 block relative">
                    <input
                        type={showPassword ? "text" : "password"}
                        name="password"
                        placeholder="Senha"
                        value={formData.password}
                        onChange={handleChange}
                        className={`w-full p-1 border rounded pr-12 ${errors.password ? "border-red-500" : ""}`}
                        required
                    />
                    <button
                        type="button"
                        className="absolute right-3 top-2"
                        onClick={togglePasswordVisibility}
                    >
                      {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
                    </button>
                    {errors.password && <span className="text-red-500 text-sm">{errors.password}</span>}
                  </label>
                  <label className="col-span-1 block relative">
                    <input
                        type={showConfirmPassword ? "text" : "password"}
                        name="confirmPassword"
                        placeholder="Confirmar Senha"
                        value={formData.confirmPassword}
                        onChange={handleChange}
                        className={`w-full p-1 border rounded pr-12 ${errors.confirmPassword ? "border-red-500" : ""}`}
                        required
                    />
                    <button
                        type="button"
                        className="absolute right-3 top-2"
                        onClick={toggleConfirmPasswordVisibility}
                    >
                      {showConfirmPassword ? <EyeOff size={20} /> : <Eye size={20} />}
                    </button>
                    {errors.confirmPassword && <span className="text-red-500 text-sm">{errors.confirmPassword}</span>}
                  </label>
                  {errors.general && <div className="text-red-500 col-span-2">{errors.general}</div>}
                  <div className="col-span-2">
                    <button
                        type="submit"
                        className="w-full p-2 bg-[#708090] text-white rounded"
                    >
                      Criar Conta
                    </button>
                  </div>


                </>
            )}


          </form>
          <div
              className="absolute bottom-0 right-0 hidden lg:block"
              style={{ marginBottom: "31px" }}
          >
          </div>
        </main>

        <footer className="p-4 text-center hidden lg:block">

        </footer>
      </section>
  );
};

export default Register;
