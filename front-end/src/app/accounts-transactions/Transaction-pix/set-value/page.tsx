"use client";

import { useState, ChangeEvent, useEffect, Suspense } from "react";
import { useSearchParams, useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { QrCode, ArrowLeft } from "lucide-react";

function SetValueContent() {
  const [amount, setAmount] = useState<string>("0,00");
  const [accountNumber, setAccountNumber] = useState<string>("");
  const [agencyNumber, setAgencyNumber] = useState<string>("");
  const [loading, setLoading] = useState<boolean>(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [userName, setUserName] = useState<string | null>(null);
  const router = useRouter();
  const searchParams = useSearchParams();

  useEffect(() => {
    const accountParam = searchParams.get("account");
    const agencyParam = searchParams.get("agency");

    if (accountParam && agencyParam) {
      setAccountNumber(accountParam);
      setAgencyNumber(agencyParam);
      fetchUserName(accountParam, agencyParam);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [searchParams]);

  const fetchUserName = async (account: string, agency: string) => {
    try {
      const token =
        typeof window !== "undefined" ? localStorage.getItem("token") : null;

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
        console.log("Dados do usuário:", data);
        setUserName(data.name || data.username || "Usuário não encontrado");
      } else {
        console.error(
          "Erro ao buscar o nome do usuário: Resposta não OK",
          response
        );
        setUserName("Usuário não encontrado");
      }
    } catch (error) {
      console.error("Erro ao buscar o nome do usuário:", error);
      setUserName("Erro ao carregar nome");
    }
  };

  const handleAmountChange = (e: ChangeEvent<HTMLInputElement>) => {
    let value = e.target.value.replace(/\D/g, "");
    if (value.length > 10) return;

    value = formatCurrency(parseFloat(value) / 100);
    setAmount(value);
    setErrorMessage(null);
  };

  const formatCurrency = (value: number) => {
    return value.toLocaleString("pt-BR", {
      style: "decimal",
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    });
  };

  const handleContinue = () => {
    const parsedAmount = parseFloat(amount.replace(".", "").replace(",", "."));
    if (isNaN(parsedAmount) || parsedAmount <= 0) {
      setErrorMessage("O valor deve ser maior que zero.");
      return;
    }

    setLoading(true);
    setTimeout(() => {
      router.push(
        `/accounts-transactions/Transaction-pix/set-value/receipt?amount=${amount}&account=${accountNumber}&agency=${agencyNumber}`
      );
      setLoading(false);
    }, 2000);
  };

  return (
    <main className="flex-grow container mx-auto p-6 relative">
      <div
        className="flex items-center mb-4 cursor-pointer"
        onClick={() => router.back()}
      >
        <ArrowLeft className="w-6 h-6 text-blue-500 mr-2" />
        <span className="text-blue-500 text-lg">Voltar</span>
      </div>

      <h1 className="text-4xl font-semibold mb-8">Definir valor</h1>

      <div className="flex justify-start">
        <Card className="w-full max-w-lg shadow-md">
          <CardHeader>
            <div className="flex items-center space-x-4">
              <QrCode className="w-10 h-10 text-blue-500" />
              <CardTitle className="text-2xl font-semibold">
                Quanto você vai enviar?
              </CardTitle>
            </div>
          </CardHeader>

          <CardContent>
            <p className="text-black-600 mb-2">
              Nome: {userName || "Carregando..."}
            </p>
            <p className="text-black-600 mb-2">Conta: {accountNumber}</p>
            <p className="text-black-600 mb-6">Agência: {agencyNumber}</p>

            <div className="flex items-center justify-center text-5xl font-bold mb-4">
              <span className="mr-2" style={{ color: "#76849eff" }}>
                R$
              </span>
              <input
                type="text"
                value={amount}
                onChange={handleAmountChange}
                className="text-5xl font-bold text-center bg-transparent outline-none focus:ring-0"
                style={{
                  width: `${Math.max(amount.length, 6)}ch`,
                  color: "#000000ff",
                }}
              />
            </div>

            {errorMessage && (
              <p className="text-red-500 text-sm mb-4">{errorMessage}</p>
            )}

            <Button
              className={`bg-[#76849eff] text-white w-full hover:bg-[#000000] transition-colors ${
                loading ? "cursor-not-allowed opacity-50" : ""
              }`}
              onClick={handleContinue}
              disabled={loading}
            >
              {loading ? "Processando..." : "Continue para revisão"}
            </Button>
          </CardContent>
        </Card>
      </div>
    </main>
  );
}

export default function SetValue() {
  return (
    <Suspense fallback={<main className="p-6">Carregando valor...</main>}>
      <SetValueContent />
    </Suspense>
  );
}
