import { Card, CardHeader } from "@/components/ui/card";
import { Banknote } from "lucide-react"; // Import the specific icon used in the first card
import Link from "next/link";

export default function HomePage() {
    // Dados do primeiro serviço
    const service = {
        title: "Histórico de Transações",
        icon: Banknote,
        href: "/account",
    };

    return (
        <main className="flex-grow container mx-auto p-4 md:p-6 lg:p-8 relative">
            <div className="flex flex-col md:flex-row gap-6">
                <section className="md:w-3/4">
                    <h2 className="text-2xl font-semibold mb-6">
                        Tenha seu banco digital e acompanhe sua conta de onde estiver!
                    </h2>
                    <div className="grid grid-cols-1 gap-6">
                        <Card className="w-100 h-64 my-0 mx-auto lg:m-0">
                            <CardHeader>
                                <div className="flex items-center gap-2">
                                    <service.icon className="w-6 h-6" />
                                    {service.title}
                                </div>
                            </CardHeader>
                        </Card>
                    </div>
                </section>
            </div>
        </main>
    );
}
