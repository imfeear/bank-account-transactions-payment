"use client";

import { useEffect, useState } from "react";
import { useSearchParams, useRouter } from "next/navigation";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Barcode } from "lucide-react";
import { generateBarCode } from "@/lib/utils";

export default function BankSlipPage() {
  const [amount, setAmount] = useState<string>("0,00");
  const [key, setKey] = useState<string>("00000");
  const [dueDate, setDueDate] = useState<string>("");
  const [agency, setAgency] = useState<string>("0000");
  const [account, setAccount] = useState<string>("0000");
  const [barcode, setBarcode] = useState<string>("");
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [copyMessage, setCopyMessage] = useState<string | null>(null);
  const searchParams = useSearchParams();
  const router = useRouter();

  const generateDueDate = () => {
    const today = new Date();
    let due = new Date(today);
    due.setDate(due.getDate() + 1);

    if (due.getDay() === 6) due.setDate(due.getDate() + 2);
    if (due.getDay() === 0) due.setDate(due.getDate() + 1);

    setDueDate(due.toLocaleDateString("pt-BR"));
  };

  const generateBarcode = async () => {
    try {
      const accountAndAgency = localStorage.getItem("AccountAndAgency");
  
      // Verifica se `accountAndAgency` é nulo antes de executar o split
      if (!accountAndAgency) {
        throw new Error("Account and Agency not found in localStorage.");
      }
  
      const [account, agency] = accountAndAgency.split("-");
  
      // Espera a resolução da Promise
      const response = await generateBarCode(account, agency, amount);
  
      if (response && response.barcode) {
        setBarcode(response.barcode); // Atualiza o estado com o barcode
      } else {
        console.error("Erro: resposta inválida ou campo 'barcode' ausente.");
      }
    } catch (error) {
      console.error("Erro ao gerar o código de barras:", error);
      setErrorMessage("Erro ao gerar o código de barras. Verifique seus dados.");
    }
  };

  useEffect(() => {
    if (searchParams) {
      const amountParam = searchParams.get("amount");
      const userKey = localStorage.getItem("userKey") || "00000";
      const accountAndAgency =
        localStorage.getItem("AccountAndAgency") || "0000-0000";
  
      // Verifica se accountAndAgency tem o formato esperado
      const [userAgency, userAccount] = accountAndAgency.includes("-")
        ? accountAndAgency.split("-")
        : ["0000", "0000"];
  
      setAmount(amountParam || "0,00");
      setKey(userKey);
      setAgency(userAgency);
      setAccount(userAccount);
  
      generateDueDate();
      generateBarcode();
    }
  }, [searchParams]);

  const copyCode = () => {
    navigator.clipboard.writeText(barcode);
    setCopyMessage("Código copiado para a área de transferência!");

    setTimeout(() => {
      setCopyMessage(null);
    }, 2000);
  };

  return (
    <main className="flex-grow container mx-auto p-8">
      <div className="mb-10">
        <h1 className="text-3xl font-semibold text-left">Boleto</h1>
      </div>

      <div className="flex justify-start">
        <Card className="w-full max-w-2xl shadow-lg p-8">
          <CardHeader className="space-y-6">
            <div
              className="flex items-center justify-center space-x-4 rounded-md shadow-md"
              style={{ padding: "2rem 0" }}
            >
              <Barcode className="w-8 h-8 text-[#575252]" />
              <CardTitle className="text-2xl font-medium">
                O boleto do depósito foi gerado
              </CardTitle>
            </div>
          </CardHeader>

          <CardContent className="space-y-8">
            <div className="text-left">
              <p
                className="font-semibold mb-1"
                style={{ fontSize: "1.35rem", color: "#575252" }}
              >
                Valor gerado
              </p>
              <p className="text-3xl font-bold text-[#033196]">R$ {amount}</p>
            </div>

            <div className="flex justify-between items-center space-x-6">
              <div>
                <p className="text-gray-500">Conta</p>
                <p className="text-center">
                  {agency}-{account}
                </p>
              </div>
              <div>
                <p className="text-gray-500">Vencimento</p>
                <p className="text-center">{dueDate}</p>
              </div>
            </div>

            <hr className="my-6" />

            <div className="flex items-center space-x-4 justify-center">
              <Barcode className="w-10 h-10 text-[#575252]" />
              <p className="font-mono break-all">{barcode}</p>
            </div>

            <div className="flex flex-col items-center space-y-4">
              <Button
                onClick={copyCode}
                className="bg-blue-600 text-white hover:bg-blue-700 transition-colors w-full"
                style={{
                  maxWidth: "300px",
                  fontSize: "1.25rem",
                  padding: "1rem 1rem",
                  height: "60px",
                }}
              >
                Copiar código
              </Button>
              {copyMessage && (
                <p className="text-green-600 text-sm mt-2">{copyMessage}</p>
              )}
              <Button
                onClick={() =>
                  alert("Código de barras enviado para o e-mail cadastrado!")
                }
                className="bg-blue-600 text-white hover:bg-blue-700 transition-colors w-full"
                style={{
                  maxWidth: "300px",
                  fontSize: "1.25rem",
                  padding: "1rem 1rem",
                  height: "60px",
                }}
              >
                Enviar por e-mail
              </Button>
            </div>

            {errorMessage && (
              <p className="text-red-500 text-center mt-4">{errorMessage}</p>
            )}
          </CardContent>
        </Card>
      </div>
    </main>
  );
}
