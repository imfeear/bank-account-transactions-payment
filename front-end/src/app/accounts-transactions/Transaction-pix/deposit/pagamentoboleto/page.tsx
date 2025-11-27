"use client";

import { Suspense, useEffect, useState } from "react";
import { useSearchParams, useRouter } from "next/navigation";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Barcode, ArrowLeft } from "lucide-react";

function PayBoletoPageContent() {
  const [barcode, setBarcode] = useState<string>("");
  const [amount, setAmount] = useState<string>("0,00");
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const searchParams = useSearchParams();
  const router = useRouter();

  useEffect(() => {
    const amountParam = searchParams.get("amount");
    if (amountParam) {
      setAmount(amountParam);
    }
  }, [searchParams]);

  const handleBarcodeChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setBarcode(e.target.value);
    setErrorMessage(null);
  };

  const handleBack = () => {
    router.back();
  };

  const handlePayBoleto = async () => {
    if (barcode.length !== 47) {
      setErrorMessage("O código do boleto deve ter 47 dígitos.");
      return;
    }

    setIsLoading(true);
    setErrorMessage(null);

    try {
      const token =
        typeof window !== "undefined" ? localStorage.getItem("token") : null;

      if (!token) {
        setErrorMessage("Usuário não autenticado. Faça login novamente.");
        setIsLoading(false);
        return;
      }

      const response = await fetch(
        "http://localhost:8081/transactions/deposit",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            barcode,
            amount: parseFloat(
              amount.replace(/\./g, "").replace(",", ".")
            ),
          }),
        }
      );

      if (!response.ok) {
        throw new Error(
          "Erro ao processar o pagamento do boleto. Tente novamente."
        );
      }

      alert("Pagamento do boleto realizado com sucesso!");

      setTimeout(() => {
        router.push("/home");
      }, 2000);
    } catch (error) {
      if (error instanceof Error) {
        setErrorMessage(error.message);
      } else {
        setErrorMessage("Erro inesperado. Tente novamente.");
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <main className="flex-grow container mx-auto p-8">
      <div
        className="flex items-center mb-6 cursor-pointer"
        onClick={handleBack}
      >
        <ArrowLeft className="w-5 h-5 text-blue-600" />
        <span className="ml-2 text-blue-600 font-medium">Voltar</span>
      </div>

      <div className="flex flex-col items-start mb-6">
        <h1 className="text-3xl font-semibold">
          Pagamento de Boleto Genérico
        </h1>
      </div>

      <div className="flex justify-start">
        <Card className="w-full max-w-md shadow-lg p-6">
          <CardHeader className="space-y-4">
            <div className="flex items-center space-x-4">
              <Barcode className="w-8 h-8 text-[#033196]" />
              <CardTitle className="text-2xl font-medium">
                Insira o código do boleto
              </CardTitle>
            </div>
          </CardHeader>

          <CardContent className="space-y-6">
            <input
              type="text"
              value={barcode}
              onChange={handleBarcodeChange}
              maxLength={47}
              placeholder="Código do boleto (47 dígitos)"
              className="w-full p-4 border rounded-lg text-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            />

            {errorMessage && (
              <p className="text-red-500 text-sm">{errorMessage}</p>
            )}

            <Button
              onClick={handlePayBoleto}
              disabled={isLoading}
              className={`bg-green-600 text-white w-full hover:bg-green-700 transition-colors ${
                isLoading ? "cursor-not-allowed opacity-50" : ""
              }`}
              style={{
                padding: "0.75rem 1rem",
                fontSize: "1rem",
                height: "60px",
              }}
            >
              {isLoading ? "Processando..." : "Pagar Boleto"}
            </Button>
          </CardContent>
        </Card>
      </div>
    </main>
  );
}

export default function PayBoletoPage() {
  return (
    <Suspense fallback={<div>Carregando pagamento de boleto...</div>}>
      <PayBoletoPageContent />
    </Suspense>
  );
}
