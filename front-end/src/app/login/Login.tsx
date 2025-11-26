"use client";

import Image from "next/image";
import { FC, useEffect, useState, FormEvent } from "react";
import { useRouter } from "next/navigation";
import logo from "@/images/bank.png";

const Login: FC = () => {
  const [accountAndAgency, setAccountAndAgency] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [error, setError] = useState<string>("");
  const [loading, setLoading] = useState<boolean>(false);
  const [isMounted, setIsMounted] = useState(false);
  const router = useRouter();

  useEffect(() => {
    setIsMounted(true);
  }, []);

  if (!isMounted) return null;

  const handleLogin = async (e: FormEvent) => {
    e.preventDefault();

    const { accountNumber, agencyNumber } =
      parseAccountAndAgency(accountAndAgency);

    try {
      setLoading(true);
      const response = await fetch("http://localhost:8081/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ accountNumber, agencyNumber, password }),
      });

      const data = await response.json();

      if (response.ok) {
        // Armazene o nome do usuário e o token
        localStorage.setItem("username", data.username);
        localStorage.setItem("token", data.token);

        // Armazene "AccountAndAgency" no localStorage
        localStorage.setItem(
          "AccountAndAgency",
          `${accountNumber}-${agencyNumber}`
        );

        // Redirecione para a página inicial
        router.push("/home");
      } else {
        setError(data.message || "Invalid credentials");
      }
    } catch (err) {
      console.error(err);
      setError("An unexpected error occurred. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  const parseAccountAndAgency = (value: string) => {
    const [account, agency] = value.split("-");
    return { accountNumber: account, agencyNumber: agency };
  };

  return (
    <div className="h-screen flex items-center justify-center relative overflow-hidden bg-[#0552FC] md:bg-white">
      <div className="absolute top-0 left-0 w-[300px] sm:w-[400px] md:w-[500px] h-[300px] sm:h-[400px] md:h-[500px] hidden md:block">
      </div>
      <div className="flex items-center justify-center h-screen w-full px-4">
        <div className="w-full max-w-[32rem] py-16 sm:py-20 px-8 sm:px-14 rounded-lg shadow-xl text-center bg-[#708090]">
          <Image
            src={logo}
            alt="New Bank Logo"
            width={100}
            height={100}
            className="mx-auto mb-4 sm:mb-6 object-contain"
            priority
          />
          <h2 className="text-white text-xl sm:text-2xl mb-4">
            Bem vindo(a) de volta.
            <br />
            Faça login para continuar.
          </h2>
          <form className="flex flex-col space-y-4" onSubmit={handleLogin}>
            <input
              type="text"
              placeholder="Account-Agency (e.g., 3930-7)"
              value={accountAndAgency}
              onChange={(e) => setAccountAndAgency(e.target.value)}
              required
              className="w-full p-3 rounded border outline-none focus:ring-2 focus:ring-blue-400"
            />
            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              className="w-full p-3 rounded border outline-none focus:ring-2 focus:ring-blue-400"
            />
            <button
              type="submit"
              className="w-full bg-white text-black-700 font-bold py-2 rounded transition hover:bg-gray-200"
              disabled={loading}
            >
              {loading ? "Processando..." : "Entrar"}
            </button>
          </form>

          {error && <p className="text-red-500 mt-2">{error}</p>}


          {/* ESQUECEU SUA SENHA REMOVIDO!
          <p className="text-white mt-2">
            <a
              onClick={() => router.push("/forgot-password")}
              className="cursor-pointer no-underline hover:underline"
            >
              Esqueceu sua senha?
            </a>
          </p>

          */}

          <p className="text-white mt-4">
            Não tem uma conta?{" "}
            <a
              onClick={() => router.push("/register")}
              className="cursor-pointer no-underline hover:underline"
            >
              Cadastre-se
            </a>
          </p>
        </div>
      </div>

      <div className="absolute bottom-0 right-0 w-[300px] sm:w-[400px] md:w-[500px] h-[200px] sm:h-[250px] md:h-[300px] hidden md:block">
      </div>
    </div>
  );
};

export default Login;
