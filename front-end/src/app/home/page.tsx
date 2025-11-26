import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
    Home,
    RefreshCcw,
    CreditCard,
    Banknote,
} from "lucide-react";
import Link from "next/link";

export default function HomePage() {
    return (
        <main className="flex-grow container mx-auto p-4 md:p-6 lg:p-8 relative">
            <div className="flex flex-col md:flex-row gap-6">
                <section className="md:w-3/4">
                    <h2 className="text-2xl font-semibold mb-6">
                        Tenha seu banco digital e acompanhe sua conta de onde estiver!
                    </h2>
                    <div className="grid grid-cols-1 lg:grid-cols-[repeat(2,minmax(0,320px))] gap-6">
                        {[
                            {
                                title: "Conta corrente",
                                icon: Home,
                                description: "Consulte aqui o seu saldo de conta corrente",
                                href: "/account",
                            },
                            {
                                title: "Transações",
                                icon: RefreshCcw,
                                description: "Consulte suas transações e seu histórico",
                                href: "/accounts-transactions",
                            },
                            {
                                title: "Cartão de crédito",
                                icon: CreditCard,
                                description:
                                    "Consulte aqui o seu cartão e a data de vencimento da sua fatura",
                                href: "/credit-card",
                            },
                            {
                                title: "Empréstimos",
                                icon: Banknote,
                                description:
                                    "Consulte aqui seu empréstimo e simule empréstimos futuros",
                                href: "/loans",
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
                                <service.icon className="h-20 w-16 text-black my-0 mx-auto"/>
                                <CardContent>
                                    <p className="text-lg text-center text-grey-700">
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