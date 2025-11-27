'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Label } from "@/components/ui/label";

export default function AppPaymentPageCodeInformation() {
  const [paymentDate, setPaymentDate] = useState('');
  const [selectedDate, setSelectedDate] = useState('');
  const router = useRouter();

  const handleConfirm = () => {
    if (paymentDate && (paymentDate === 'hoje' || selectedDate)) {
      router.push('/payments/payment-code/information-code/aprove-payment');
    } else {
      router.push('/payments/payment-code/information-code/reproved-payment');
    }
  };

  return (
    <Card className="w-full max-w-md mx-auto bg-white shadow-lg">
      <CardContent className="p-6">
        <h2 className="text-xl font-semibold mb-6 text-gray-800">
          Informações do pagamento
        </h2>

        <div className="space-y-4 mb-6">
          <div className="flex justify-between">
            <span className="text-gray-600">Vencimento:</span>
            <span className="font-medium text-gray-800">25/11/2024</span>
          </div>
          <div className="flex justify-between">
            <span className="text-gray-600">Pagamento:</span>
            <span className="font-medium text-gray-800">25/10/2024</span>
          </div>
          <div className="flex justify-between">
            <span className="text-gray-600">Valor:</span>
            <span className="font-semibold text-gray-800">R$ 5,00</span>
          </div>
          <div className="flex justify-between">
            <span className="text-gray-600">Saldo:</span>
            <span className="font-semibold text-gray-800">R$ 100,00</span>
          </div>
        </div>

        <div className="border-t border-gray-200 pt-4 mb-6">
          <div className="flex justify-between mb-2">
            <span className="text-gray-600">ID:</span>
            <span className="text-gray-800">2</span>
          </div>
          <div className="flex justify-between">
            <span className="text-gray-600">Data e hora do pagamento:</span>
            <span className="text-gray-800">13/10/2024 - 18:15:50</span>
          </div>
        </div>

        {/* Escolha da data de pagamento */}
        <div className="mb-6" aria-label="Escolha a data de pagamento">
          <div className="flex items-center space-x-2 mb-2">
            <input
              type="radio"
              id="hoje"
              name="paymentDate"
              value="hoje"
              checked={paymentDate === 'hoje'}
              onChange={() => setPaymentDate('hoje')}
              className="w-4 h-4"
            />
            <Label htmlFor="hoje" className="text-gray-800">
              Hoje 25/10/2024
            </Label>
          </div>

          <div className="flex items-center space-x-2">
            <input
              type="radio"
              id="agendar"
              name="paymentDate"
              value="agendar"
              checked={paymentDate === 'agendar'}
              onChange={() => setPaymentDate('agendar')}
              className="w-4 h-4"
            />
            <Label htmlFor="agendar" className="text-gray-800">
              Agendar
            </Label>
          </div>

          {paymentDate === 'agendar' && (
            <div className="mt-4">
              <input
                type="date"
                value={selectedDate}
                onChange={(e) => setSelectedDate(e.target.value)}
                className="w-full p-2 border border-gray-300 rounded"
                min={new Date().toISOString().split('T')[0]}
              />
            </div>
          )}
        </div>

        <Button
          onClick={handleConfirm}
          className="w-full bg-blue-600 hover:bg-blue-700 text-white"
          aria-label="Confirmar pagamento"
        >
          Confirmar
        </Button>
      </CardContent>
    </Card>
  );
}
