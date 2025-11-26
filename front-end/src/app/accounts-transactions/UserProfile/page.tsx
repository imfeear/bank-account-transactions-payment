"use client";

import { useState, useEffect } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardHeader, CardContent } from "@/components/ui/card";
import { Edit, LockKeyhole } from "lucide-react";

export default function UserProfile() {
  const [passwordConfirmation, setPasswordConfirmation] = useState("");
  const [showPasswordField, setShowPasswordField] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [userData, setUserData] = useState<{
    password: string;
    email: string;
    phoneNumber: string;
    address: {
      street: string;
      neighborhood: string;
      cep: string;
      number: string;
      state: string;
      city: string;
      complement: string;
    };
    naturalPerson: {
      fullName: string;
      cpf: string;
      bornDate: string;
    };
    legalEntity: {
      razaoSocial: string;
      cnpj: string;
      responsible: string;
    } | null;
  }>({
    password: "",
    email: "",
    phoneNumber: "",
    address: {
      street: "",
      neighborhood: "",
      cep: "",
      number: "",
      state: "",
      city: "",
      complement: "",
    },
    naturalPerson: {
      fullName: "",
      cpf: "",
      bornDate: "",
    },
    legalEntity: null, // Inicializa como null
  });

  
  
  const getInputValue = (value: any): string | number => {
    if (value === null || value === undefined) {
      return ""; // Converte `null` ou `undefined` para string vazia
    }
    return value;
  };
  

  const [loading, setLoading] = useState(false);
  const [editing, setEditing] = useState<{
    fullName: boolean;
    cpf: boolean;
    cep: boolean;
    street: boolean;
    neighborhood: boolean;
    number: boolean;
    state: boolean;
    city: boolean;
    complement: boolean;
    phoneNumber: boolean;
    email: boolean;
    password: boolean;
  }>({
    fullName: false,
    cpf: false,
    cep: false,
    street: false,
    neighborhood: false,
    number: false,
    state: false,
    city: false,
    complement: false,
    phoneNumber: false,
    email: false,
    password: false,
  });
  

  const fetchUserData = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch("http://localhost:8081/account/details", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });
  
      if (response.ok) {
        const data = await response.json();
  
        // Substitui `null` por strings vazias antes de armazenar no estado
        setUserData({
          ...data,
          password: data.password ?? "",
          email: data.email ?? "",
          phoneNumber: data.phoneNumber ?? "",
          address: {
            street: data.address?.street ?? "",
            neighborhood: data.address?.neighborhood ?? "",
            cep: data.address?.cep ?? "",
            number: data.address?.number ?? "",
            state: data.address?.state ?? "",
            city: data.address?.city ?? "",
            complement: data.address?.complement ?? "",
          },
          naturalPerson: {
            fullName: data.naturalPerson?.fullName ?? "",
            cpf: data.naturalPerson?.cpf ?? "",
            bornDate: data.naturalPerson?.bornDate ?? "",
          },
          legalEntity: data.legalEntity ?? null,
        });
      } else {
        console.error("Erro ao buscar dados do usuário:", response);
      }
    } catch (error) {
      console.error("Erro ao buscar dados do usuário:", error);
    }
  };
  

  useEffect(() => {
    fetchUserData();
  }, []);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
  
    // Verifica se o campo pertence ao legalEntity
    if (userData.legalEntity && name in userData.legalEntity) {
      setUserData((prev) => ({
        ...prev,
        legalEntity: {
          ...prev.legalEntity!,
          [name]: value,
        },
      }));
    } else if (name in userData.naturalPerson) {
      // Verifica se o campo pertence ao naturalPerson
      setUserData((prev) => ({
        ...prev,
        naturalPerson: {
          ...prev.naturalPerson,
          [name]: value,
        },
      }));
    } else if (name in userData.address) {
      // Verifica se o campo pertence ao endereço
      setUserData((prev) => ({
        ...prev,
        address: {
          ...prev.address,
          [name]: value,
        },
      }));
    } else {
      // Caso contrário, atualiza campos de nível superior
      setUserData((prev) => ({
        ...prev,
        [name]: value,
      }));
    }
  };
  
  
  

  const handleEditClick = (field: keyof typeof editing) => {
    setEditing((prevEditing) => ({
      ...prevEditing,
      [field]: !prevEditing[field], // Alterna entre habilitado/desabilitado
    }));
  };
  

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
  
    try {
      const token = localStorage.getItem("token");
  
      const payload = {
        password: userData.password || null,
        email: userData.email || null,
        phoneNumber: userData.phoneNumber || null,
        address: {
          street: userData.address.street,
          neighborhood: userData.address.neighborhood,
          cep: userData.address.cep,
          number: userData.address.number,
          state: userData.address.state,
          city: userData.address.city,
          complement: userData.address.complement,
        },
        naturalPerson: {
          fullName: userData.naturalPerson.fullName,
          cpf: userData.naturalPerson.cpf,
          bornDate: userData.naturalPerson.bornDate,
        },
        legalEntity: userData.legalEntity,
      };

      
  
      const response = await fetch("http://localhost:8081/account/update", {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(payload), // Ajustado para usar `payload` diretamente aqui
      });
      
      if (response.ok) {
        alert("Dados atualizados com sucesso!");
      } else {
        const error = await response.json();
        alert(`Erro ao atualizar os dados: ${error.message}`);
      }
      
    } catch (error) {
      console.error("Erro ao atualizar os dados:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteAccount = async () => {
    if (!passwordConfirmation) {
      alert("Por favor, insira sua senha para confirmar a exclusão da conta.");
      return;
    }

    const confirmDelete = confirm("Tem certeza que deseja deletar sua conta? Esta ação não pode ser desfeita.");
    if (confirmDelete) {
      try {
        const token = localStorage.getItem("token");
        const response = await fetch("http://localhost:8081/account/deleteAccount", {
          method: "DELETE",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            password: passwordConfirmation,
          }),
        });

        if (response.ok) {
          alert("Conta deletada com sucesso!");
          window.location.href = "/login";
        } else {
          const error = await response.json();
          alert(`Erro ao deletar a conta: ${error.message}`);
        }
      } catch (error) {
        console.error("Erro ao deletar a conta:", error);
      } finally {
        setShowModal(false); // Fecha o modal após a tentativa
      }
    }
  };
  

  return (
    <main className="flex-grow container mx-auto p-6 relative">
      <h1 className="text-2xl font-semibold mb-6">Alterar Dados do Usuário</h1>

      <div className="flex justify-start">
        <Card className="w-full max-w-2xl shadow-md">
          <CardHeader className="pb-0"></CardHeader>

          <CardContent>
            <form onSubmit={handleSubmit}>
{/* Informações Pessoais */}
<div className="mb-4">
  <h2 className="text-lg font-semibold">Informações Pessoais</h2>
  {userData.legalEntity ? (
    // Caso seja uma pessoa jurídica
    <div className="space-y-3">
      {/* Razão Social */}
      <div className="relative">
        <label htmlFor="razaoSocial" className="block text-sm font-medium text-gray-700">
          Razão Social
        </label>
        <Input
          id="razaoSocial"
          type="text"
          name="razaoSocial"
          placeholder="Razão Social"
          value={userData.legalEntity.razaoSocial || ""}
          onChange={(e) =>
            setUserData((prev) => ({
              ...prev,
              legalEntity: {
                ...prev.legalEntity!,
                razaoSocial: e.target.value,
              },
            }))
          }
          className="w-full pl-10"
          disabled={!editing.fullName}
        />
        <Edit
          className="absolute left-3 top-[40px] transform -translate-y-1/2 text-gray-500 w-5 h-5 cursor-pointer"
          onClick={() => handleEditClick("fullName")}
        />
      </div>

      {/* CNPJ */}
      <div className="relative">
        <label htmlFor="cnpj" className="block text-sm font-medium text-gray-700">
          CNPJ
        </label>
        <Input
          id="cnpj"
          type="text"
          name="cnpj"
          placeholder="CNPJ"
          value={userData.legalEntity.cnpj || ""}
          className="w-full pl-10"
          disabled // Campo desabilitado
        />
        <LockKeyhole className="absolute left-3 top-[40px] transform -translate-y-1/2 text-gray-500 w-5 h-5" />
      </div>
    </div>
  ) : (
    // Caso seja uma pessoa física
    <div className="space-y-3">
      {/* Nome Completo */}
      <div className="relative">
        <label htmlFor="fullName" className="block text-sm font-medium text-gray-700">
          Nome Completo
        </label>
        <Input
          id="fullName"
          type="text"
          name="fullName"
          placeholder="Nome Completo"
          value={userData.naturalPerson.fullName || ""}
          onChange={handleInputChange}
          className="w-full pl-10"
          disabled={!editing.fullName}
        />
        <Edit
          className="absolute left-3 top-[40px] transform -translate-y-1/2 text-gray-500 w-5 h-5 cursor-pointer"
          onClick={() => handleEditClick("fullName")}
        />
      </div>

              {/* CPF */}
                        <div className="relative">
                          <label htmlFor="cpf" className="block text-sm font-medium text-gray-700">
                            CPF
                          </label>
                          <Input
                            id="cpf"
                            type="text"
                            name="cpf"
                            placeholder="CPF"
                            value={userData.naturalPerson.cpf || ""}
                            className="w-full pl-10"
                            disabled // Campo desabilitado
                          />
                          <LockKeyhole className="absolute left-3 top-[40px] transform -translate-y-1/2 text-gray-500 w-5 h-5" />
                        </div>
                      </div>
                    )}
                  </div>

              {/* Endereço */}
              <div className="mb-4">
                      <h2 className="text-lg font-semibold">Endereço</h2>
                      {["street", "neighborhood", "cep", "number", "state", "city", "complement"].map((field) => (
                        <div key={field} className="flex-1 mb-3 relative">
                          <Input
                            type="text"
                            name={field}
                            placeholder={field.charAt(0).toUpperCase() + field.slice(1)}
                            value={getInputValue(userData.address[field as keyof typeof userData.address])}
                            onChange={(e) =>
                              setUserData((prev) => ({
                                ...prev,
                                address: {
                                  ...prev.address,
                                  [field]: e.target.value,
                                },
                              }))
                            }
                            className="w-full pl-10"
                            disabled={!editing[field as keyof typeof editing]} // Controla a edição
                          />
                          <Edit
                            className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-500 w-5 h-5 cursor-pointer"
                            onClick={() => handleEditClick(field as keyof typeof editing)}
                          />
                        </div>
                      ))}
                    </div>
              {/* Contato e Acesso */}
              <div className="mb-4">
                <h2 className="text-lg font-semibold">Contato e Acesso</h2>
                {["password", "email", "phoneNumber"].map((field) => (
            <div key={field} className="flex-1 mb-3 relative">
              <Input
                type="text"
                name={field}
                placeholder={field.charAt(0).toUpperCase() + field.slice(1)}
                value={getInputValue(userData[field as keyof typeof userData])}
                onChange={(e) =>
                  setUserData((prev) => ({
                    ...prev,
                    [field]: e.target.value,
                  }))
                }
                className="w-full pl-10"
                disabled={!editing[field as keyof typeof editing]} // Controla a edição
              />
              <Edit
                className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-500 w-5 h-5 cursor-pointer"
                onClick={() => handleEditClick(field as keyof typeof editing)}
              />
            </div>
          ))}
        </div>

          {/* Botão de Atualizar Dados */}
                <Button
                  type="submit"
                  className="w-full bg-blue-500 text-white hover:bg-blue-600 active:bg-blue-700 transition-colors mb-1"
                  disabled={loading}
                >
                  {loading ? "Processando..." : "Atualizar Dados"}
                </Button>

              {/* Botão para abrir o modal de deleção */}
              <Button
                type="button"
                className="w-full bg-red-500 text-white hover:bg-red-600 active:bg-red-700 transition-colors mt-4"
                onClick={() => setShowModal(true)}
              >
                Deletar Conta
              </Button>
            </form>
          </CardContent>
        </Card>
      </div>

      {/* Modal de confirmação */}
      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-lg shadow-lg w-96">
            <h2 className="text-xl font-semibold mb-4">Confirmar Deleção</h2>
            <p className="text-gray-600 mb-4">
              Para deletar sua conta, por favor, confirme sua senha abaixo.
            </p>
            <Input
              id="passwordConfirmation"
              type="password"
              placeholder="Digite sua senha"
              value={passwordConfirmation}
              onChange={(e) => setPasswordConfirmation(e.target.value)}
              className="w-full mb-4"
            />
            <div className="flex justify-end space-x-4">
              <Button
                type="button"
                className="bg-gray-500 text-white hover:bg-gray-600 transition-colors px-4 py-2 rounded"
                onClick={() => setShowModal(false)} // Fecha o modal
              >
                Cancelar
              </Button>
              <Button
                type="button"
                className="bg-red-500 text-white hover:bg-red-600 transition-colors px-4 py-2 rounded"
                onClick={handleDeleteAccount} // Confirma a deleção
              >
                Confirmar Deleção
              </Button>
            </div>
          </div>
        </div>
      )}
    </main>
  );
}
