import { clsx, type ClassValue } from "clsx";
import { twMerge } from "tailwind-merge";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export async function fetchUsers() {
  const response = await fetch("http://localhost:8081/api/users");
  if (!response.ok) {
    throw new Error("Erro ao buscar usuários");
  }
  return await response.json();
}

export async function fetchTransactions() {
  const response = await fetch("http://localhost:8081/transaction/statement", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
  if (!response.ok) {
    throw new Error("Erro ao buscar transações");
  }
  return await response.json();
}

export async function fetchNotifications() {
  try {
    const response = await fetch("http://localhost:8081/notifications", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`, // Inclua o token, se necessário
      },
    });

    if (!response.ok) {
      console.error("Erro HTTP ao buscar notificações:", response.status);
      return []; // Retorna um array vazio como fallback
    }

    try {
      return await response.json(); // Retorna as notificações
    } catch (jsonError) {
      console.error("Erro ao converter resposta para JSON:", jsonError);
      return []; // Retorna um array vazio em caso de falha no JSON
    }
  } catch (error) {
    console.error("Erro ao buscar notificações:", error);
    return []; // Retorna um array vazio em caso de erro
  }
}

export async function clearNotifications() {
  try {
    const response = await fetch("http://localhost:8081/notifications", {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`, // Inclua o token, se necessário
      },
    });

    if (!response.ok) {
      console.error("Erro HTTP ao limpar notificações:", response.status);
      return false; // Retorna falso como fallback
    }

    return true; // Indica sucesso
  } catch (error) {
    console.error("Erro ao limpar notificações:", error);
    return false; // Retorna falso em caso de erro
  }
}

export async function fetchAccountBalance() {
  try {
    const response = await fetch("http://localhost:8081/account/balance", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });

    if (!response.ok) {
      console.error("Erro HTTP ao buscar o saldo:", response.status);
      return { balance: 0 }; // Retorna um saldo padrão
    }

    try {
      return await response.json(); // Retorna o saldo
    } catch (jsonError) {
      console.error("Erro ao converter resposta para JSON:", jsonError);
      return { balance: 0 }; // Retorna um saldo padrão em caso de falha no JSON
    }
  } catch (error) {
    console.error("Erro ao buscar o saldo:", error);
    return { balance: 0 }; // Retorna um saldo padrão em caso de erro
  }
}

export async function generateBarCode(
  accountNumber: string,
  agencyNumber: string,
  value: string
) {
  try {
    const response = await fetch(
      "http://localhost:8081/api/payments/generate-barcode",
      {
        method: "POST",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
        body: JSON.stringify({
          accountNumber: accountNumber,
          agencyNumber: agencyNumber,
          value: Number(value),
        }),
      }
    );

    if (!response.ok) {
      console.error("Erro ao gerar o barcode:", response);
    }

    return await response.json();
  } catch (error) {
    console.error("Erro ao gerar o barcode:", error);
  }
}
