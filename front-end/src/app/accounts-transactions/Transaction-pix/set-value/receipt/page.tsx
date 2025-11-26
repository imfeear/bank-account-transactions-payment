"use client";

import { useState, useEffect } from "react";
import { useSearchParams, useRouter } from "next/navigation";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { CheckCircle, ArrowLeft, XCircle } from "lucide-react";
import jsPDF from "jspdf";
import logo from "@/images/Jala-Bank.png";

export default function Receipt() {
  const [isConfirmed, setIsConfirmed] = useState<boolean>(false);
  const [amount, setAmount] = useState<string>("0,00");
  const [transactionDate, setTransactionDate] = useState<string>("");
  const [showError, setShowError] = useState<boolean>(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [accountNumber, setAccountNumber] = useState<string>("");
  const [agencyNumber, setAgencyNumber] = useState<string>("");
  const [userName, setUserName] = useState<string>("");
  const [loading, setLoading] = useState<boolean>(false);
  const [transactionId, setTransactionId] = useState<string | null>(null);
  const router = useRouter();
  const searchParams = useSearchParams();
  const [password, setPassword] = useState<string>(""); // Adicione o estado para a senha
  const [showPasswordModal, setShowPasswordModal] = useState<boolean>(false); // Para exibir o modal de senha
  const [loginAttempts, setLoginAttempts] = useState<number>(0); // Contador de tentativas
  useEffect(() => {
    const amountParam = searchParams.get("amount");
    const accountParam = searchParams.get("account");
    const agencyParam = searchParams.get("agency");
    const transactionIdParam = searchParams.get("transactionId"); // Recebe o ID da transação

    if (amountParam) setAmount(amountParam);
    if (accountParam) setAccountNumber(accountParam);
    if (agencyParam) setAgencyNumber(agencyParam);
    if (transactionIdParam) setTransactionId(transactionIdParam); // Define o ID da transação

    if (accountParam && agencyParam) {
      fetchUserName(accountParam, agencyParam);
    }
  }, [searchParams]);

  const fetchUserName = async (account: string, agency: string) => {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(
        `http://localhost:8081/account/user?accountNumber=${account}&agencyNumber=${agency}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (response.ok) {
        const data = await response.json();
        setUserName(data.name || "Usuário não encontrado");
      } else {
        setUserName("Usuário não encontrado");
      }
    } catch (error) {
      console.error("Erro ao buscar o nome do usuário:", error);
      setUserName("Erro ao carregar nome");
    }
  };

  const handleConfirmation = async () => {
    setShowPasswordModal(true); // Exibir o modal para entrada de senha
  };

  const confirmAuth = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`http://localhost:8081/auth/confirmAuth`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          username: userName, // Nome do usuário
          password: password, // Senha digitada
        }),
      });

      if (!response.ok) {
        // Incrementar o número de tentativas
        setLoginAttempts((prev) => prev + 1);

        if (loginAttempts + 1 >= 3) {
          setErrorMessage(
            "Você excedeu o limite de tentativas. Redirecionando para o login."
          );
          setTimeout(() => {
            router.push("/login"); // Redireciona para a página de login após 3 tentativas
          }, 3000);
        } else {
          setErrorMessage(
            `Senha incorreta. Você tem ${
              3 - (loginAttempts + 1)
            } tentativa(s) restante(s).`
          );
        }

        return false;
      }

      // Se a autenticação for bem-sucedida, fecha o modal e reseta o contador
      setShowPasswordModal(false);
      setLoginAttempts(0);
      setErrorMessage(null);
      return true;
    } catch (error) {
      console.error("Erro na autenticação:", error);
      setErrorMessage("Erro ao conectar com o servidor. Tente novamente.");
      return false;
    }
  };

  const handleTransfer = async () => {
    const isAuthConfirmed = await confirmAuth(); // Chama a autenticação primeiro
    if (!isAuthConfirmed) return;

    const parsedAmount = parseFloat(amount.replace(".", "").replace(",", "."));
    if (isNaN(parsedAmount) || parsedAmount <= 0) {
      setErrorMessage("O valor deve ser maior que zero.");
      return;
    }

    setLoading(true);

    try {
      const token = localStorage.getItem("token");

      const response = await fetch(
        `http://localhost:8081/transaction/transfer`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            destinationAccountNumber: accountNumber,
            destinationAgencyNumber: agencyNumber,
            amount: parsedAmount,
          }),
        }
      );

      if (!response.ok) {
        const data = await response.json();
        setErrorMessage(data.message || "Erro ao realizar a transferência.");
        return;
      }

          // Simulação de geração de ID de transação
    const transactionData = await response.json(); // Supondo que o backend retorna um ID de transação
    setTransactionId(transactionData.transactionId || Math.floor(Math.random() * 1000000).toString());

      setTransactionDate(
        new Date().toLocaleString("pt-BR", {
          day: "2-digit",
          month: "2-digit",
          year: "numeric",
          hour: "2-digit",
          minute: "2-digit",
          second: "2-digit",
        })
      );
      setErrorMessage(null); // Limpa mensagem de erro se a transferência foi bem-sucedida
      setIsConfirmed(true);
    } catch (error) {
      console.error("Erro ao realizar a transferência:", error);
      setErrorMessage("Erro ao realizar a transferência.");
    } finally {
      setLoading(false);
    }
  };

  const handleDownloadReceipt = () => {
    const doc = new jsPDF();

    // Valores dinâmicos para o comprovante
    const transferAmount = amount; // Valor digitado pelo usuário
    const originAccount =
      localStorage.getItem("AccountAndAgency") || "Conta não encontrada"; // Substitua por variável com dados reais da conta de origem
    const destinationAccount = `${accountNumber}-${agencyNumber}`; // Conta de destino preenchida dinamicamente

    // Definir dimensões e posição da imagem
    const imgWidth = 50;
    const imgHeight = 20;
    const imgX = 10;
    const imgY = 10;

    // Título principal abaixo da imagem
    doc.setFontSize(16);
    doc.setTextColor(0, 0, 0);
    doc.text(
      "Comprovante de transferência entre contas do New Bank - TEV",
      10,
      50
    );

    // Subtítulo
    doc.setFontSize(12);
    doc.setTextColor(100); // Cinza para o subtítulo
    doc.text("Via Internet Banking New Bank", 10, 58);

    // Informações do comprovante
    doc.setFontSize(11);
    doc.setTextColor(0);

    const details = [
      { label: "Emitente:", value: "New Bank S.A" },
      { label: "Conta origem:", value: originAccount }, // Conta de origem dinâmica
      { label: "Conta destino:", value: destinationAccount }, // Conta de destino dinâmica
      { label: "Nome destinatário:", value: userName || "Destinatário" }, // Nome do destinatário dinâmico
      { label: "Valor:", value: `R$ ${transferAmount}` }, // Valor dinâmico
      {
        label: "Identificação da operação:",
        value: transactionId || "ID Não gerado",
      }, // ID da transação
      {
        label: "Data da operação:",
        value: new Date().toLocaleDateString("pt-BR"),
      },
      {
        label: "Data/hora da operação:",
        value: new Date().toLocaleString("pt-BR"),
      },
      { label: "Chave de segurança:", value: "E4298E8077F943FE912345678" },
    ];

    let yOffset = 70; // Posição inicial para o conteúdo
    details.forEach((item) => {
      doc.text(`${item.label} ${item.value}`, 10, yOffset);
      yOffset += 8;
    });

    // Mensagem de confirmação
    doc.setFontSize(10);
    doc.setTextColor(0);
    doc.text(
      "DÉBITO REALIZADO COM SUCESSO. A PREVISÃO DO CRÉDITO NA CONTA DESTINO É DE 30 MINUTOS.",
      10,
      yOffset + 10
    );

    // Baixar o PDF
    doc.save("Comprovante_JalaBank.pdf");
  };

  const closeErrorModal = () => {
    setShowError(false);
  };

  return (
    <main className="flex-grow container mx-auto p-6 relative">
      {!isConfirmed && (
        <div
          className="flex items-center mb-4 cursor-pointer"
          onClick={() => router.back()}
        >
          <ArrowLeft className="w-6 h-6 text-blue-500 mr-2" />
          <span className="text-blue-500 text-lg">Voltar</span>
        </div>
      )}
  
      <h1 className="text-3xl font-semibold mb-6">Recibo</h1>
  
      <div className="flex justify-start">
        <Card className="w-full max-w-md shadow-lg rounded-lg">
          {isConfirmed && (
            <div className="flex items-center bg-green-50 p-4 rounded-md mb-4">
              <CheckCircle className="w-6 h-6 text-green-500 mr-2" />
              <p className="text-green-600 font-medium">
                Pronto! Seu pagamento foi realizado.
              </p>
            </div>
          )}
  
          <CardContent>
            <div className="text-gray-600 mb-4">
              <p>
                Valor pago: <span className="text-black-600">R$ {amount}</span>
              </p>
              <p>
                Para: <span className="text-black-600">{userName}</span>
              </p>
              <p>
                Conta: <span className="text-black-600">{accountNumber}</span>
              </p>
              <p>
                Agência: <span className="text-black-600">{agencyNumber}</span>
              </p>
            </div>
  
            <hr className="my-4" />
  
            <div className="text-gray-600 mb-4">
              <p>ID/Transação: {transactionId || "Aguardando confirmação"}</p>
              <p>
                Data e hora da transação:{" "}
                {isConfirmed ? transactionDate : "Aguardando confirmação"}
              </p>
            </div>
  
            <div className="mt-6 flex flex-col space-y-4">
                  {!isConfirmed ? (
                    <Button
                      className="bg-[#708090] text-white hover:bg-blue-600 transition-colors"
                      onClick={handleConfirmation}
                    >
                      Confirmar
                    </Button>
                  ) : (
                    <Button
                      className="bg-[#708090] text-white hover:bg-black-600 transition-colors"
                      onClick={handleDownloadReceipt}
                    >
                      Baixar Comprovante
                    </Button>
                  )}
                </div>
          </CardContent>
        </Card>
      </div>
  
      {showPasswordModal && (
        <div className="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-lg shadow-lg max-w-sm w-full">
            <h2 className="text-lg font-semibold mb-4">Confirme sua senha</h2>
            <input
              type="password"
              className="w-full p-2 border rounded mb-4"
              placeholder="Digite sua senha"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            <Button
              className="bg-[#708090] text-white w-full"
              onClick={handleTransfer}
            >
              Confirmar
            </Button>
            {errorMessage && (
              <p className="text-red-500 text-sm mt-4">{errorMessage}</p>
            )}
          </div>
        </div>
      )}
  
      {showError && (
        <div className="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-lg shadow-lg max-w-sm w-full">
            <div className="flex items-center space-x-2 mb-4">
              <XCircle className="w-6 h-6 text-red-500" />
              <h2 className="text-lg font-semibold text-red-500">
                Saldo insuficiente
              </h2>
            </div>
            <p className="text-gray-600 mb-4">Verifique o saldo da sua conta!</p>
            <Button
              className="w-full bg-red-500 text-white"
              onClick={closeErrorModal}
            >
              Fechar
            </Button>
          </div>
        </div>
      )}
    </main>
  );}  