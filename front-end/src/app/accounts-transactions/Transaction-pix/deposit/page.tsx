/* eslint-disable @typescript-eslint/no-unused-vars */
"use client";

import { useState, ChangeEvent } from "react";
import { useRouter } from "next/navigation";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Banknote, ArrowLeft } from "lucide-react";

export default function DepositPage() {
  const [amount, setAmount] = useState<string>("0,00");
  const [boletoCode, setBoletoCode] = useState<string>(""); // Mantemos isso para a próxima etapa
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const router = useRouter();

  const handleAmountChange = (e: ChangeEvent<HTMLInputElement>) => {
    let value = e.target.value.replace(/\D/g, "");
    if (value.length > 8) return;
    value = (parseFloat(value) / 100).toLocaleString("pt-BR", {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    });
    setAmount(value);
    setErrorMessage(null);
  };

  const handleBoletoCodeChange = (e: ChangeEvent<HTMLInputElement>) => {
    setBoletoCode(e.target.value);
  };

  const handleContinue = () => {
    const parsedAmount = parseFloat(
      amount.replace(/\./g, "").replace(",", ".")
    );

    if (isNaN(parsedAmount) || parsedAmount < 5) {
      setErrorMessage("O valor mínimo para depósito é R$ 5,00.");
      return;
    }

    // Redireciona para a próxima página com o valor
    router.push(
      `/accounts-transactions/Transaction-pix/deposit/bank-slips?amount=${amount}`
    );
  };

  const handleBack = () => {
    router.back();
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

      <div className="flex flex-col md:flex-row gap-8 items-start mb-6">
        <h1 className="text-3xl font-semibold">Depósito</h1>
      </div>

      <div className="flex justify-start">
        <Card className="w-full max-w-md shadow-lg">
          <CardHeader className="flex flex-row items-center space-x-4">
            <Banknote className="w-12 h-12 text-[#033196]" />
            <CardTitle className="text-3xl font-bold">
              Qual valor do depósito?
            </CardTitle>
          </CardHeader>

          <CardContent className="text-center space-y-6">
            <div className="flex justify-center items-center text-5xl font-bold">
              <span className="mr-2" style={{ color: "#033196" }}>
                R$
              </span>
              <input
                type="text"
                value={amount}
                onChange={handleAmountChange}
                className="text-5xl font-bold text-center bg-transparent outline-none focus:ring-0"
                style={{
                  width: `${Math.max(amount.length, 6)}ch`,
                  color: "#033196",
                }}
              />
            </div>
            <p className="text-gray-500">Insira um valor mínimo de R$ 5,00</p>

            {errorMessage && (
              <p className="text-red-500 text-sm">{errorMessage}</p>
            )}

            <Button
              onClick={handleContinue}
              className="bg-blue-600 text-white w-full hover:bg-blue-700 transition-colors"
              style={{
                padding: "0.75rem 1rem",
                fontSize: "1rem",
                height: "60px",
              }}
            >
              Continue para revisão
            </Button>
          </CardContent>
        </Card>
      </div>
    </main>
  );
}
