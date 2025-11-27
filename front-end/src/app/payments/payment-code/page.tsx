'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Barcode } from 'lucide-react';

export default function AppPaymentPageCode() {
    const [barcodeInput, setBarcodeInput] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const router = useRouter();

    const handleContinue = async () => {
        setIsLoading(true);
        try {
            const response = await fetch("http://localhost:8081/api/payments/confirm-payment", {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ barcodeText: barcodeInput }),
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => null);
                const errorMessage = errorData?.error || `Erro ${response.status}: ${response.statusText}`;
                alert(errorMessage);
                return;
            }

            router.push('/payments/payment-code/information-payment');
            } catch (error: any) {
            alert(`Erro ao processar o pagamento: ${error?.message || ''}`);
            } finally {
            setIsLoading(false);
            }
        };

    return (
        <main className="flex-1 p-8">
            <h1 className="text-2xl font-bold mb-6">Pagamento de boletoo</h1>
            <div className="bg-white shadow-md rounded-lg p-6 max-w-2xl">
                <div className="flex items-center mb-4">
                    <Barcode className="w-6 h-6 mr-2 text-blue-600" />
                    <h2 className="text-xl font-semibold">Pagar com código de barras</h2>
                </div>
                <p className="text-sm text-blue-600 mb-4">
                    Digite, cole ou use um leitor de código de barras para pagar ou agendar sua conta.
                </p>
                <div className="flex flex-col sm:flex-row space-y-4 sm:space-y-0 sm:space-x-4">
                    <Input
                        type="text"
                        value={barcodeInput}
                        onChange={(e) => setBarcodeInput(e.target.value)}
                        placeholder="00000.00000 00000.000000 00000.000000 0 00000000000000"
                        className="flex-1"
                    />
                    <Button
                        onClick={handleContinue}
                        disabled={isLoading}
                        className="bg-blue-600 hover:bg-blue-700 text-white"
                    >
                        {isLoading ? 'Processando...' : 'Continuar'}
                    </Button>
                </div>
            </div>
        </main>
    );
}
