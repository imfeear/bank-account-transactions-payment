"use client";

import { useState, useEffect } from "react";
import { Card, CardHeader, CardContent } from "@/components/ui/card";
import { Banknote, ArrowDownLeft, ArrowUpRight, HelpCircle } from "lucide-react"; // Adicionado ícone para transações desconhecidas
import { fetchTransactions } from "@/lib/utils";

interface Transaction {
  id: number;
  transactionType: string;
  amount: number;
  transactionDate: string;
  description: string;
  originAccountNumber: number | null; // Pode ser null
  destinationAccountNumber: number | null; // Pode ser null
  status: string;
}

export default function Statement() {
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [loading, setLoading] = useState(true);

  const currentAccountDetails = localStorage.getItem("AccountAndAgency");
  const currentAccountNumber = currentAccountDetails?.split("-")[0];

  useEffect(() => {
    const fetchTransactionsData = async () => {
      try {
        const transactionsData = await fetchTransactions();
        setTransactions(transactionsData);
      } finally {
        setLoading(false);
      }
    };

    fetchTransactionsData();
  }, []);

  const formatDate = (dateString: string | number | Date): string => {
    const date = new Date(dateString);
    const day = date.getDate().toString().padStart(2, "0");
    const month = (date.getMonth() + 1).toString().padStart(2, "0");
    const year = date.getFullYear();
    const hours = date.getHours().toString().padStart(2, "0");
    const minutes = date.getMinutes().toString().padStart(2, "0");

    return `${day}/${month}/${year} ${hours}:${minutes}`;
  };

  const getTransactionStyle = (transaction: Transaction) => {
    const isOutgoing =
      transaction.originAccountNumber?.toString() === currentAccountNumber; // Envio
    const isIncoming =
      transaction.destinationAccountNumber?.toString() === currentAccountNumber; // Recebimento

    const type = transaction.transactionType?.toUpperCase();

    // Lógica para tratar tipos de transações conhecidos
    if (type === "DEPOSITO") {
      return {
        color: "text-green-500",
        icon: ArrowUpRight,
        label: "Depósito",
      };
    } else if (type === "PIX") {
      if (isIncoming) {
        return {
          color: "text-green-500",
          icon: Banknote,
          label: "Transferência PIX (Recebida)",
        };
      } else {
        return {
          color: "text-red-500",
          icon: Banknote,
          label: "Transferência PIX (Enviada)",
        };
      }
    } else if (type === "TRANSFERENCIA") {
      if (isIncoming) {
        return {
          color: "text-green-500",
          icon: ArrowUpRight,
          label: "Transferência (Recebida)",
        };
      } else {
        return {
          color: "text-red-500",
          icon: ArrowDownLeft,
          label: "Transferência (Enviada)",
        };
      }
    }

    // Caso o tipo não seja reconhecido, trata como desconhecido
    return {
      color: "text-gray-500",
      icon: HelpCircle,
      label: "Transação Desconhecida",
    };
  };

  return (
    <main className="flex-grow container mx-auto p-4 md:p-6 lg:p-8 relative">
      <div className="flex flex-col md:flex-row gap-6">
        <section className="md:w-3/4">
          <h2 className="text-2xl font-semibold mb-6">
            Histórico de Transações
          </h2>
          <div className="grid grid-cols-1 gap-6">
            <Card className="w-full min-h-64 my-0 mx-auto lg:m-0">
              <CardHeader>
                <div className="flex items-center gap-2">
                  <Banknote className="w-6 h-6" />
                  Extrato
                </div>
              </CardHeader>
              <CardContent>
                {loading ? (
                  <div className="flex justify-center items-center h-32">
                    <p>Carregando transações...</p>
                  </div>
                ) : transactions.length === 0 ? (
                  <div className="flex justify-center items-center h-32">
                    <p>Nenhuma transação encontrada</p>
                  </div>
                ) : (
                  <div className="space-y-4">
                    {transactions.map((transaction) => {
                      const { icon: Icon, color, label } =
                        getTransactionStyle(transaction);

                      return (
                        <div
                          key={transaction.id}
                          className="flex items-center justify-between p-4 border rounded-lg hover:bg-gray-50"
                        >
                          <div className="flex items-center gap-4">
                            <Icon className={`w-6 h-6 ${color}`} />
                            <div>
                              <p className="font-semibold">{label}</p>
                              <p className="text-sm text-gray-500">
                                {formatDate(transaction.transactionDate)}
                              </p>
                            </div>
                          </div>
                          <p className={`font-semibold ${color}`}>
                            {color === "text-green-500" ? "+" : "-"} R${" "}
                            {transaction.amount.toFixed(2)}
                          </p>
                        </div>
                      );
                    })}
                  </div>
                )}
              </CardContent>
            </Card>
          </div>
        </section>
      </div>
    </main>
  );
}
