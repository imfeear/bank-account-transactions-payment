"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { QrCode, Search } from "lucide-react";

interface Transaction {
  id: number;
  account: string;
  agency: string;
  name: string;
}

export default function TransactionPix() {
  const [loading, setLoading] = useState(false);
  const [accountNumber, setAccountNumber] = useState("");
  const [agencyNumber, setAgencyNumber] = useState("");
  const [recentTransactions, setRecentTransactions] = useState<Transaction[]>(
    []
  );
  const [filteredTransactions, setFilteredTransactions] = useState<
    Transaction[]
  >([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [userName, setUserName] = useState<string | null>(null);
  const [visibleCount, setVisibleCount] = useState(5);
  const [errorMessage, setErrorMessage] = useState<string | null>(null); // Novo estado para a mensagem de erro
  const router = useRouter();

  useEffect(() => {
    const savedTransactions = JSON.parse(
      localStorage.getItem("recentTransactions") || "[]"
    );

    const fetchUserNames = async (transactions: Transaction[]) => {
      const updatedTransactions = await Promise.all(
        transactions.map(async (transaction) => {
          const userName = await fetchUserName(
            transaction.account,
            transaction.agency
          );
          return { ...transaction, name: userName };
        })
      );
      setRecentTransactions(updatedTransactions);
      setFilteredTransactions(updatedTransactions);
      localStorage.setItem(
        "recentTransactions",
        JSON.stringify(updatedTransactions)
      );
    };

    fetchUserNames(savedTransactions);
  }, []);

  const fetchUserName = async (
    account: string,
    agency: string
  ): Promise<string> => {
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
        return data.name || data.username || "Usuário não encontrado";
      } else {
        console.error(
          "Erro ao buscar o nome do usuário: Resposta não OK",
          response
        );
        return "Usuário não encontrado";
      }
    } catch (error) {
      console.error("Erro ao buscar o nome do usuário:", error);
      return "Erro ao carregar nome";
    }
  };

  const handlePayment = async () => {
    if (!accountNumber || !agencyNumber) return;

    setLoading(true);
    const userName = await fetchUserName(accountNumber, agencyNumber);
    setLoading(false);

    if (
      userName === "Usuário não encontrado" ||
      userName === "Erro ao carregar nome"
    ) {
      setErrorMessage(
        "Conta ou agência incorretos. Verifique os dados e tente novamente."
      ); // Exibe a mensagem de erro
      return;
    }

    setErrorMessage(null); // Limpa a mensagem de erro se os dados estiverem corretos

    const newTransaction: Transaction = {
      id: new Date().getTime(),
      account: accountNumber,
      agency: agencyNumber,
      name: userName,
    };

    const updatedTransactions = [...recentTransactions, newTransaction];
    setRecentTransactions(updatedTransactions);
    localStorage.setItem(
      "recentTransactions",
      JSON.stringify(updatedTransactions)
    );

    router.push(
      `/accounts-transactions/Transaction-pix/set-value?account=${accountNumber}&agency=${agencyNumber}`
    );
  };

  const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setSearchTerm(value);

    const filtered = recentTransactions.filter(
      (transaction) =>
        transaction.name.toLowerCase().includes(value.toLowerCase()) ||
        transaction.agency.includes(value)
    );

    setFilteredTransactions(filtered);
  };

  const handleSendAnotherValue = (account: string, agency: string) => {
    router.push(
      `/accounts-transactions/Transaction-pix/set-value?account=${account}&agency=${agency}`
    );
  };

  const handleLoadMore = () => {
    setVisibleCount((prevCount) => prevCount + 5);
  };

  return (
    <main className="flex-grow container mx-auto p-6 relative">
      <h1 className="text-3xl font-semibold mb-8">
        Pagamento via Transferência
      </h1>

      <div className="flex justify-start">
        <Card className="w-full max-w-2xl shadow-md">
          <CardHeader className="pb-0">
            <div className="flex items-center space-x-4">
              <QrCode className="w-8 h-8 text-blue-500" />
              <CardTitle className="text-2xl font-bold">
                Realizar Transferência
              </CardTitle>
            </div>
          </CardHeader>

          <CardContent>
            <p className="text-sm text-gray-500 mt-2">
              Digite ou cole o número da conta e da agência que deseja
              transferir
            </p>

            <div className="flex space-x-2 mt-4">
              <Input
                type="text"
                placeholder="Número da conta (4 dígitos)"
                className="flex-grow"
                value={accountNumber}
                onChange={(e) => setAccountNumber(e.target.value)}
                maxLength={4}
              />
              <Input
                type="text"
                placeholder="Número da agência (1-2 dígitos)"
                className="flex-grow"
                value={agencyNumber}
                onChange={(e) => setAgencyNumber(e.target.value)}
                maxLength={2}
              />
              <Button
                className={`bg-[#000000] text-white hover:bg-[#033196] transition-colors ${
                  loading ? "cursor-not-allowed opacity-50" : ""
                }`}
                onClick={handlePayment}
                disabled={
                  loading ||
                  !accountNumber ||
                  !agencyNumber ||
                  accountNumber.length !== 4 ||
                  agencyNumber.length < 1 ||
                  agencyNumber.length > 2
                }
              >
                {loading ? "Processando..." : "Continuar"}
              </Button>
            </div>

            {/* Mensagem de erro */}
            {errorMessage && (
              <p className="text-red-500 mt-2">{errorMessage}</p>
            )}
          </CardContent>
        </Card>
      </div>
      <div className="mt-4 flex items-center">
        <Input
          type="text"
          placeholder="Pesquisar por nome ou agência"
          className="flex-grow mb-4 h-8 pr-8"
          value={searchTerm}
          onChange={handleSearchChange}
          style={{ maxWidth: "300px", marginRight: "8px" }}
        />
        <Search className="h-4 w-4" />
      </div>

      {/* Seção de Transferências Recentes */}
      <div className="mt-8">
        <h2 className="text-xl font-semibold mb-4">Transferências Recentes</h2>
        <div className="space-y-4">
          {filteredTransactions.slice(0, visibleCount).map((transaction) => (
            <Card key={transaction.id} className="p-4 shadow-sm">
              <div className="flex justify-between items-center">
                <div>
                  <p className="text-sm text-gray-500">
                    Conta: {transaction.account}
                  </p>
                  <p className="text-sm text-gray-500">
                    Agência: {transaction.agency}
                  </p>
                  <p className="text-sm text-gray-500">
                    Para: {transaction.name}
                  </p>
                </div>
                <Button
                  className="bg-[#708090] text-white hover:bg-[#000000] transition-colors"
                  onClick={() =>
                    handleSendAnotherValue(
                      transaction.account,
                      transaction.agency
                    )
                  }
                >
                  Enviar outro valor
                </Button>
              </div>
            </Card>
          ))}
        </div>
        {/* Botão para carregar mais transferências */}
        {filteredTransactions.length > visibleCount && (
          <div className="flex justify-center mt-4">
            <Button
              className="bg-blue-500 text-white hover:bg-[#033196] transition-colors"
              onClick={handleLoadMore}
            >
              Carregar mais transferências
            </Button>
          </div>
        )}
      </div>
    </main>
  );
}
