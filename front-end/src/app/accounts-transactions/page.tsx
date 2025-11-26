"use client";

import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { RefreshCcw, Banknote, HandCoins } from "lucide-react";
import Link from "next/link";

/*
    As linhas comentadas abaixo não estavam sendo usadas no app. Pf verificar a necessidade delas
*/

// import { useState } from "react";

export default function AccountsTransactions() {
  // const [isBalanceVisible, setIsBalanceVisible] = useState(false);

  // const token = localStorage.getItem('token'); // Recupera o token dinamicamente

  // const toggleBalanceVisibility = () => {
  //     setIsBalanceVisible(!isBalanceVisible);
  // };

  // Função para formatar o valor com duas casas decimais no formato brasileiro

  return (
    <main className="flex-grow container mx-auto p-4 md:p-6 lg:p-8 relative">
      <div className="relative flex flex-col md:flex-row gap-6">
        <section className="md:w-3/4">
          <h2 className="text-2xl font-semibold mb-6">
            Realize transferências de forma rápida e segura
          </h2>
          <div className="grid grid-cols-1 lg:grid-cols-[repeat(2,minmax(0,320px))] gap-6">
            {[
              {
                title: "Depósito",
                icon: HandCoins,
                description: "Faça aqui o seu depósito",
                href: "/accounts-transactions/Transaction-pix/deposit",
              },
              {
                title: "Transações",
                icon: RefreshCcw,
                description: "Faça suas transações.",
                href: "/accounts-transactions/Transaction-pix",
              },
              {
                title: "Extrato",
                icon: Banknote,
                description: "Consulte aqui seu extrato bancário",
                href: "/statement",
              },
            ].map((service, index) => (
              <Card className="w-80 h-64 my-0 mx-auto lg:m-0" key={index}>
                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                  <CardTitle className="text-xl font-medium">
                    {service.title}
                  </CardTitle>
                  <Button variant="link" size="lg" className="mt-2 p-0">
                    <Link href={service.href}>Exibir</Link>
                  </Button>
                </CardHeader>
                <service.icon className="h-20 w-16 text-black my-0 mx-auto" />
                <CardContent>
                  <p className="text-lg text-center text-black-700">
                    {service.description}
                  </p>
                </CardContent>
              </Card>
            ))}
          </div>
        </section>
      </div>
    </main>
  );
}
