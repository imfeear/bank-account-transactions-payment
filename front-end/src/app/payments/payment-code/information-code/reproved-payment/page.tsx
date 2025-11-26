'use client';

import { Card, CardContent } from "@/components/ui/card";
import { XCircle } from "lucide-react";

export default function AppPaymentPageReproved() {
  return (
    <main className="flex-1 p-8 relative">
      <Card className="w-full max-w-md mx-auto bg-white shadow-lg">
        <CardContent className="p-6">
          <div className="text-center">
            <div className="mb-4 flex justify-center">
              <div className="rounded-full bg-red-100 p-3">
                <XCircle className="h-8 w-8 text-red-600" />
              </div>
            </div>
            <h2 className="text-xl font-semibold mb-2 text-gray-800">Pagamento Negado</h2>
            <p className="text-gray-600">Desculpe, não foi possível processar seu pagamento.</p>
            <p className="text-gray-600 mt-2">Por favor, verifique suas informações e tente novamente.</p>
          </div>
        </CardContent>
      </Card>
    </main>
  );
}
