'use client'

import { useState } from 'react'
import { CreditCard } from 'lucide-react'
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"

export default function Page() {
  const [pixKey, setPixKey] = useState('') // Chave PIX de destino
  const [amount, setAmount] = useState('') // Valor da transferência
  const [errorMessage, setErrorMessage] = useState('') // Mensagem de erro
  const [successMessage, setSuccessMessage] = useState('') // Mensagem de sucesso
  const [loading, setLoading] = useState(false) // Estado de carregamento

  // Manipular entrada da chave PIX
  const handlePixKeyChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPixKey(e.target.value)
  }

  // Manipular entrada do valor
  const handleAmountChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setAmount(e.target.value.replace(/[^\d.,]/g, '')) // Apenas números, vírgulas e pontos
  }

  // Lógica para transferência PIX
  const handleTransfer = async () => {
    setErrorMessage('')
    setSuccessMessage('')
    
    const parsedAmount = parseFloat(amount.replace(".", "").replace(",", "."))
    if (isNaN(parsedAmount) || parsedAmount <= 0) {
      setErrorMessage("O valor deve ser maior que zero.")
      return
    }

    if (!pixKey.trim()) {
      setErrorMessage("A chave PIX é obrigatória.")
      return
    }

    setLoading(true)

    try {
      const token = localStorage.getItem("token") // Token de autenticação

      const response = await fetch(`http://localhost:8081/transaction/transfer-by-pix`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          destinationPixKey: pixKey.trim(), // Chave PIX de destino
          amount: parsedAmount, // Valor da transferência
        }),
      })

      if (!response.ok) {
        const data = await response.json()
        setErrorMessage(data.message || "Erro ao realizar a transferência.")
        return
      }

      setSuccessMessage("Transferência realizada com sucesso!")
      setPixKey('')
      setAmount('')
    } catch (error) {
      setErrorMessage("Erro ao conectar com o servidor.")
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="flex justify-start items-center min-h-screen bg-gray-50 px-12">
      <div className="bg-white p-8 rounded-lg shadow-md w-full max-w-md">
        <h2 className="text-2xl font-bold mb-6 text-center">Transferência via PIX</h2>
        <div className="space-y-4">
          {/* Entrada da chave PIX */}
          <div>
            <div className="flex items-center mb-2">
              <CreditCard className="mr-2 text-blue-600" />
              <label htmlFor="pix-key" className="block text-sm font-medium text-gray-700">
                Chave PIX de destino
              </label>
            </div>
            <Input
              id="pix-key"
              type="text"
              placeholder="Digite a chave PIX (email)"
              value={pixKey}
              onChange={handlePixKeyChange}
              className="w-full bg-gray-100"
            />
          </div>

          {/* Entrada do valor */}
          <div>
            <div className="flex items-center mb-2">
              <CreditCard className="mr-2 text-blue-600" />
              <label htmlFor="amount" className="block text-sm font-medium text-gray-700">
                Valor da transferência
              </label>
            </div>
            <Input
              id="amount"
              type="text"
              placeholder="Digite o valor (ex: 100.00)"
              value={amount}
              onChange={handleAmountChange}
              className="w-full bg-gray-100"
            />
          </div>

          {/* Mensagem de erro */}
          {errorMessage && (
            <p className="text-sm text-red-600 text-center border border-red-300 p-2 rounded bg-red-50">
              {errorMessage}
            </p>
          )}

          {/* Mensagem de sucesso */}
          {successMessage && (
            <p className="text-sm text-green-600 text-center border border-green-300 p-2 rounded bg-green-50">
              {successMessage}
            </p>
          )}

          {/* Botão de Transferência */}
          <Button
            onClick={handleTransfer}
            disabled={loading}
            className={`w-full ${loading ? 'bg-gray-400' : 'bg-blue-600 hover:bg-blue-700'} text-white`}
          >
            {loading ? 'Processando...' : 'Transferir'}
          </Button>
        </div>
      </div>
    </div>
  )
}
