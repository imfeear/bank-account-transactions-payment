'use client';

import { Card, CardContent } from "@/components/ui/card";
import { Check } from 'lucide-react'; 

export default function AppPaymentPageAprove() {
  return (
    <main className="flex-1 p-8 relative">
      {/* Header area, if needed /}
      {/ <h1 className="text-2xl font-bold mb-6 text-gray-800">Informações</h1> */}

      <Card className="w-full max-w-md mx-auto bg-white shadow-lg">
        <CardContent className="p-6">
          <div className="text-center">
            <div className="mb-4 flex justify-center">
              <div className="rounded-full bg-green-100 p-3">
                <Check className="h-8 w-8 text-green-600" />
              </div>
            </div>
            <h2 className="text-xl font-semibold mb-2 text-gray-800">
              Sucesso
            </h2>
            <p className="text-gray-600">
              Pronto! Seu pagamento foi realizado.
            </p>
          </div>
        </CardContent>
      </Card>
    </main>
  );
}