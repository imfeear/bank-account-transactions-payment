"use client";

import React, { useState } from "react";
import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { toast } from "@/hooks/use-toast";

const BACKEND_URL = "http://localhost:8081";

export default function SubmitTransaction() {
    const router = useRouter();

    const [formData, setFormData] = useState({
        cardDetails: {
            holderName: "",
            cardNumber: "",
            cvv: "",
            expiryDate: "",
        },
        transactionDetails: {
            value: "",
            installments: 1,
            local: "",
            description: "",
            date: "",
        },
        targetDetails: {
            bankAccount: "",
            bankAgency: "",
        },
    });

    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleInputChange = (
        e: React.ChangeEvent<HTMLInputElement>,
        section: keyof typeof formData,
        field: string
    ) => {
        const value = e.target.value;
        setFormData((prev) => ({
            ...prev,
            [section]: {
                ...prev[section],
                [field]: value,
            },
        }));
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setIsSubmitting(true);

        try {
            // Ajustar o formato de expiryDate para o esperado pelo backend
            const [month, year] = formData.cardDetails.expiryDate.split("/");
            const formattedExpiryDate = `${year}-${month}-01`; // Convertendo para YYYY-MM-DD

            const requestData = {
                ...formData,
                cardDetails: {
                    ...formData.cardDetails,
                    expiryDate: formattedExpiryDate, // Data formatada
                },
            };

            const response = await fetch(`${BACKEND_URL}/cards/transfer-request`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(requestData),
            });

            const data = await response.json();

            if (response.ok) {
                toast({
                    title: "Transação Enviada!",
                    description: "Os dados foram enviados com sucesso.",
                    variant: "success",
                });
                router.push("/login");
            } else {
                toast({
                    title: "Erro!",
                    description: data.message || "Erro ao enviar a transação.",
                    variant: "destructive",
                });
            }
        } catch (error) {
            console.error("Error during submission:", error);
            toast({
                title: "Erro de Conexão!",
                description: "Não foi possível conectar ao servidor.",
                variant: "destructive",
            });
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <main className="container mx-auto p-6">
            <h1 className="text-2xl font-bold text-center mb-8">Nova Transação</h1>
            <form onSubmit={handleSubmit} className="space-y-6">
                <section>
                    <h2 className="text-xl font-semibold">Detalhes do Cartão</h2>
                    <div className="space-y-4">
                        <div>
                            <Label htmlFor="holderName">Nome do Titular</Label>
                            <Input
                                id="holderName"
                                type="text"
                                placeholder="Fulano D Tal"
                                value={formData.cardDetails.holderName}
                                onChange={(e) => handleInputChange(e, "cardDetails", "holderName")}
                                required
                                pattern="[A-Za-zÀ-ÿ ]+"
                                title="Use letras maiúsculas e minúsculas como no cartão"
                            />
                        </div>
                        <div>
                            <Label htmlFor="cardNumber">Número do Cartão (sem espaços)</Label>
                            <Input
                                id="cardNumber"
                                type="text"
                                placeholder="1234567812345678"
                                value={formData.cardDetails.cardNumber}
                                onChange={(e) => handleInputChange(e, "cardDetails", "cardNumber")}
                                required
                                pattern="\d{16}"
                                title="Digite 16 dígitos sem espaços"
                            />
                        </div>
                        <div>
                            <Label htmlFor="cvv">CVV (3 dígitos)</Label>
                            <Input
                                id="cvv"
                                type="password"
                                placeholder="123"
                                value={formData.cardDetails.cvv}
                                onChange={(e) => handleInputChange(e, "cardDetails", "cvv")}
                                required
                                pattern="\d{3}"
                                title="Digite exatamente 3 dígitos"
                            />
                        </div>
                        <div>
                            <Label htmlFor="expiryDate">Data de Expiração</Label>
                            <Input
                                id="expiryDate"
                                type="text"
                                placeholder="MM/YYYY"
                                value={formData.cardDetails.expiryDate}
                                onChange={(e) =>
                                    handleInputChange(e, "cardDetails", "expiryDate")
                                }
                                required
                                pattern="^(0[1-9]|1[0-2])\/\d{4}$"
                                title="Formato deve ser MM/YYYY"
                            />
                        </div>
                    </div>
                </section>

                {/* Detalhes da Transação */}
                <section>
                    <h2 className="text-xl font-semibold">Detalhes da Transação</h2>
                    <div className="space-y-4">
                        <div>
                            <Label htmlFor="value">Valor</Label>
                            <Input
                                id="value"
                                type="number"
                                placeholder="1000"
                                value={formData.transactionDetails.value}
                                onChange={(e) => handleInputChange(e, "transactionDetails", "value")}
                                required
                                min={1}
                            />
                        </div>
                        <div>
                            <Label htmlFor="installments">Parcelas</Label>
                            <Input
                                id="installments"
                                type="number"
                                placeholder="1"
                                value={formData.transactionDetails.installments}
                                onChange={(e) =>
                                    handleInputChange(e, "transactionDetails", "installments")
                                }
                                required
                                min={1}
                            />
                        </div>
                        <div>
                            <Label htmlFor="local">Local</Label>
                            <Input
                                id="local"
                                type="text"
                                placeholder="Loja ou local"
                                value={formData.transactionDetails.local}
                                onChange={(e) => handleInputChange(e, "transactionDetails", "local")}
                                required
                            />
                        </div>
                        <div>
                            <Label htmlFor="description">Descrição</Label>
                            <Input
                                id="description"
                                type="text"
                                placeholder="Descrição da compra"
                                value={formData.transactionDetails.description}
                                onChange={(e) =>
                                    handleInputChange(e, "transactionDetails", "description")
                                }
                                required
                            />
                        </div>
                        <div>
                            <Label htmlFor="date">Data</Label>
                            <Input
                                id="date"
                                type="date"
                                value={formData.transactionDetails.date}
                                onChange={(e) => handleInputChange(e, "transactionDetails", "date")}
                                required
                            />
                        </div>
                    </div>
                </section>

                {/* Detalhes do Destinatário */}
                <section>
                    <h2 className="text-xl font-semibold">Detalhes do Destinatário</h2>
                    <div className="space-y-4">
                        <div>
                            <Label htmlFor="bankAccount">Conta Bancária</Label>
                            <Input
                                id="bankAccount"
                                type="number"
                                placeholder="Número da conta"
                                value={formData.targetDetails.bankAccount}
                                onChange={(e) => handleInputChange(e, "targetDetails", "bankAccount")}
                                required
                                min={1}
                            />
                        </div>
                        <div>
                            <Label htmlFor="bankAgency">Agência Bancária</Label>
                            <Input
                                id="bankAgency"
                                type="number"
                                placeholder="Número da agência"
                                value={formData.targetDetails.bankAgency}
                                onChange={(e) => handleInputChange(e, "targetDetails", "bankAgency")}
                                required
                                min={1}
                            />
                        </div>
                    </div>
                </section>

                <Button type="submit" className="w-full" disabled={isSubmitting}>
                    {isSubmitting ? "Enviando..." : "Enviar"}
                </Button>
            </form>
        </main>
    );
}
