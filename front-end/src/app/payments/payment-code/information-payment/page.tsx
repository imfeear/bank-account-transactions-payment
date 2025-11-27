'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { format, isValid } from 'date-fns';
import { ptBR } from 'date-fns/locale';

type PaymentDetails = {
  barcode: string;
  dueDate: string;
  value: number;
};

export default function AppPaymentPageCodeInformation() {
  const [paymentDetails, setPaymentDetails] = useState<PaymentDetails | null>(null);
  const [paymentDate, setPaymentDate] = useState('hoje');
  const [selectedDate, setSelectedDate] = useState('');
  const [minDate, setMinDate] = useState('');
  const [todayFormatted, setTodayFormatted] = useState('');
  const [isLoading, setIsLoading] = useState(true);
  const router = useRouter();

  useEffect(() => {
    const today = new Date();
    const formattedToday = format(today, 'yyyy-MM-dd');
    setMinDate(formattedToday);
    setSelectedDate(formattedToday);
    setTodayFormatted(formatDisplayDate(formattedToday));

    const fetchPaymentDetails = async () => {
      setIsLoading(true);
      try {
        const response = await fetch('http://localhost:8081/api/payments/proof-payment', {
          method: 'GET',
          headers: { 'Content-Type': 'application/json' },
        });

          if (response.ok) {
            const data = (await response.json()) as PaymentDetails;
            setPaymentDetails(data);
          } else {
          alert('Erro ao buscar informações do pagamento.');
          router.push('/payments/payment-code'); // Redireciona em caso de erro no servidor
        }
      } catch (error) {
        console.error('Erro ao conectar-se ao servidor:', error);
        alert('Erro ao conectar-se ao servidor.');
      } finally {
        setIsLoading(false);
      }
    };
    fetchPaymentDetails();
  }, [router]);

  const handleConfirm = async () => {
    const payload = {
      barcodeText: paymentDetails?.barcode,
      datePayment: paymentDate === 'hoje' ? 'Hoje' : format(new Date(selectedDate), 'MM/dd/yyyy'),
    };

    try {
      const response = await fetch('http://localhost:8080/api/payments/process-payment', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload),
      });

      if (response.ok) {
        const result = await response.json();
        console.log("Payment Processed:", result);
        router.push('/payments/payment-code/information-code/aprove-payment');
      } else {
        const errorData = await response.json();
        console.error("Payment Error:", errorData);
        router.push('/payments/payment-code/information-code/reproved-payment');
      }
    } catch (error) {
      console.error("Network Error:", error);
      router.push('/payments/payment-code/information-code/reproved-payment');
    }
  };

    const formatDisplayDate = (dateString?: string | null) => {
      if (!dateString) return '';

      const date = new Date(dateString);
      return isValid(date) ? format(date, 'dd/MM/yyyy', { locale: ptBR }) : '';
    };

  if (isLoading) {
    return <p>Carregando informações...</p>;
  }

  if (!paymentDetails) {
    return <p>Informações do pagamento não disponíveis.</p>;
  }

  return (
      <Card className="w-full max-w-md mx-auto bg-white shadow-lg">
        <CardContent className="p-6">
          <h2 className="text-xl font-semibold mb-6 text-gray-800">Informações do pagamento</h2>

          <div className="space-y-4 mb-6">
            <div className="flex justify-between">
              <span className="text-gray-600">Vencimento:</span>
              <span className="font-medium text-gray-800">{paymentDetails.dueDate}</span>
            </div>
            <div className="flex justify-between">
              <span className="text-gray-600">Valor:</span>
              <span className="font-semibold text-gray-800">R$ {paymentDetails.value}</span>
            </div>
          </div>

          {/* Escolha da data de pagamento */}
          <div className="mb-6">
            <fieldset>
              <legend className="sr-only">Escolha a data de pagamento</legend>
              <div className="flex items-center space-x-2 mb-2">
                <input
                    type="radio"
                    id="hoje"
                    name="paymentDate"
                    value="hoje"
                    checked={paymentDate === 'hoje'}
                    onChange={() => {
                      setPaymentDate('hoje');
                      setSelectedDate(minDate);
                    }}
                    className="w-4 h-4"
                />
                <Label htmlFor="hoje" className="text-gray-800">
                  Hoje ({todayFormatted})
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
                  Agendar pagamento
                </Label>
              </div>
            </fieldset>

            {paymentDate === 'agendar' && (
                <div className="mt-4">
                  <Label htmlFor="scheduledDate" className="block mb-2">
                    Data agendada
                  </Label>
                  <Input
                      type="date"
                      id="scheduledDate"
                      value={selectedDate}
                      onChange={(e) => setSelectedDate(e.target.value)}
                      min={minDate}
                      className="w-full"
                  />
                </div>
            )}
          </div>

          <Button
              onClick={handleConfirm}
              className="w-full bg-blue-600 hover:bg-blue-700 text-white"
          >
            Confirmar Pagamento
          </Button>
        </CardContent>
      </Card>
  );
}
