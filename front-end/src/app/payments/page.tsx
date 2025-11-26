'use client'

import { Button } from "@/components/ui/button"
import Link from 'next/link'
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { QrCode, Barcode, Key } from 'lucide-react'

export default function PageMenu() {

    return (
        <main className="flex-1 p-8">
            <h1 className="text-2xl font-bold mb-6">Menu de Pagamentos</h1>
            <div className="grid grid-cols-1 lg:grid-cols-[repeat(2,minmax(0,320px))] gap-6">
                {[
                    {
                        title: "Pix",
                        icon: QrCode,
                        description: "Pague com Pix",
                        href: "/payments/payment-pix", // Caminho correto
                    },
                    {
                        title: "Pagamento boleto",
                        icon: Barcode,
                        description: "Pague seu boleto",
                        href: "/payments/payment-code/",
                    },

                    {
                        title: "Chaves cadastradas",
                        icon: Key,
                        description: "Gerencie suas chaves Pix (somente emails)",
                        href: "/payments/pix-keys", // Novo menu para gerenciar chaves Pix
                    }
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
        </main>
    )
}
