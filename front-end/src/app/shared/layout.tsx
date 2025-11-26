"use client";

import React, { useState, useEffect, useRef } from "react";
import { usePathname, useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import {
  Home,
  RefreshCcw,
  CreditCard,
  Banknote,
  LogOut,
  Menu,
  X,
  Eye,
  EyeOff,
  Bell,
  BellRing,
} from "lucide-react";
import Image from "next/image";
import Link from "next/link";
import bank from "@/images/bank.png";
import userPic from "@/images/user-pic.png";
import { fetchAccountBalance, fetchNotifications } from "@/lib/utils";

const asidebarItems = [
  { icon: Home, label: "Home", href: "/home" },
  { icon: RefreshCcw, label: "Transações", href: "/accounts-transactions" },
  { icon: CreditCard, label: "Cartão de crédito", href: "/credit-card" },
  { icon: Banknote, label: "Pagamentos", href: "/payments" },
  { icon: Banknote, label: "Empréstimos", href: "/loans" },
];

export function SharedLayout({ children }: { children: React.ReactNode }) {
  const [isAsidebarOpen, setIsAsidebarOpen] = useState(false);
  const [username, setUsername] = useState<string>("Usuário");
  const [agency, setAgency] = useState<string>("--");
  const [account, setAccount] = useState<string>("--");
  const [balance, setBalance] = useState<number | null>(null);
  const [isBalanceVisible, setIsBalanceVisible] = useState(false);
  const [isUserMenuOpen, setIsUserMenuOpen] = useState(false);
  const [notification, haveNotification] = useState(false);
  const pathname = usePathname();
  const router = useRouter();
  const userMenuRef = useRef<HTMLDivElement | null>(null);

  const fetchUsername = async () => {
    const storedUsername = localStorage.getItem("username");
    setUsername(storedUsername || "Usuário");

    const storedAgency = localStorage.getItem("AccountAndAgency");
    if (storedAgency) {
      const [agency, account] = storedAgency.split("-");
      setAgency(agency);
      setAccount(account);
    }
  };

  const haveNotifications = async () => {
    const notifications = await fetchNotifications();
    haveNotification(notifications.length > 0);
  };

  const fetchBalance = async () => {
    const balance = await fetchAccountBalance();
    setBalance(balance.balance);
  };

  const toggleBalanceVisibility = () => {
    setIsBalanceVisible((prev) => !prev);
  };

  const toggleUserMenu = () => {
    setIsUserMenuOpen((prev) => !prev);
  };

  useEffect(() => {
    const initialize = async () => {
      await fetchUsername();
      await fetchBalance();
      await haveNotifications();
    };

    initialize();

    const balanceInterval = setInterval(() => {
      fetchBalance();
    }, 60000);

    const notificationInterval = setInterval(() => {
      haveNotifications();
    }, 20000);

    return () => {
      clearInterval(balanceInterval);
      clearInterval(notificationInterval);
    };
  }, [pathname]);

  const toggleAsidebar = () => setIsAsidebarOpen((prev) => !prev);

  const handleLogout = () => {
    localStorage.clear();
    router.push("/login");
  };

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (userMenuRef.current && !userMenuRef.current.contains(event.target as Node)) {
        setIsUserMenuOpen(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  if (pathname === "/login" || pathname === "/register") {
    return <>{children}</>;
  }

  const shouldHideAsidebar =
      pathname === "/loans/simulation" ||
      pathname === "/loans/history" ||
      pathname === "/loans/contract" ||
      pathname == "/loans/payments_loans" ||
      pathname == "/loans/payments_loans_authentication";

  return (
    <section className="flex flex-col min-h-screen">
      <header className="bg-[#708090] text-white fixed top-0 left-0 w-full z-50 shadow-md">
        <div className="container mx-auto flex justify-between items-center p-4">
        <Link href="/home">
            <Image
              src={bank}
              alt="Logo do New Bank"
              width={50}
              height={50}
              className="cursor-pointer"
            />
          </Link>
          <nav className="hidden md:flex space-x-4">
            <Link href="/services" className="hover:underline">
              Serviços
            </Link>
            <Link href="/help" className="hover:underline">
              Ajuda
            </Link>
            <Link href="/about" className="hover:underline">
              Sobre
            </Link>
            <Link href="/notifications" className="flex items-center hover:text-white">
              {notification ? (
                <BellRing className="h-4 w-4 text-white" />
              ) : (
                <Bell className="h-4 w-4 text-white" />
              )}
            </Link>
          </nav>
          <div className="flex items-center space-x-4">
            <div className="relative">
              <Image
                src={userPic}
                alt="Imagem do usuário"
                width={30}
                height={30}
                className="cursor-pointer"
                onClick={toggleUserMenu}
              />
              {isUserMenuOpen && (
                <div
                  ref={userMenuRef}
                  className="absolute top-[35px] right-0 bg-white shadow-md rounded-md p-4 z-50 w-40"
                >
                  <Link href="/accounts-transactions/UserProfile" className="block text-blue-600 no-underline">
                    Editar Perfil
                  </Link>
                </div>
              )}
            </div>
            <div>
              <p className="text-md font-semibold">{username}</p>
              <div className="flex space-x-2">
                <p className="text-sm">Ag: {agency}</p>
                <p className="text-sm">C/c: {account}</p>
              </div>
            </div>
            <div className="rounded-xl px-2 py-1 flex items-center space-x-2">
              <p className="text-white text-md font-semibold">
                {isBalanceVisible
                  ? `R$ ${balance?.toLocaleString("pt-BR", {
                      minimumFractionDigits: 2,
                      maximumFractionDigits: 2,
                    })}`
                  : "R$ *******"}
              </p>
              <button onClick={toggleBalanceVisibility} className="text-white px-1">
                {isBalanceVisible ? (
                  <Eye className="h-4 w-4 text-white" />
                ) : (
                  <EyeOff className="h-4 w-4 text-white" />
                )}
              </button>
            </div>
            <Button size="sm" onClick={handleLogout}>
              <LogOut className="mr-2 h-4 w-4" /> Sair
            </Button>
          </div>
          <Button className="md:hidden" onClick={toggleAsidebar}>
            {isAsidebarOpen ? <X /> : <Menu />}
          </Button>
        </div>
      </header>

      <div className="flex flex-grow pt-16">
        {!shouldHideAsidebar && (
            <aside
                className={`fixed top-20 left-0 h-[calc(100vh-4rem)] w-64${
                    isAsidebarOpen
                        ? "block absolute top-16 left-0 h-full bg-white z-40 w-64 shadow-lg"
                        : "hidden"
                } md:block`}
            >
              <nav className="p-4">
                <ul className="space-y-2">
                  {asidebarItems.map((item, index) => (
                      <li key={index}>
                        <Link
                            href={item.href}
                            className={`flex flex-col justify-center items-center w-48 h-24 rounded transition-colors duration-200 ${
                                pathname === item.href
                                    ? "bg-blue-100 shadow-md shadow-blue-500/50"
                                    : "hover:bg-gray-100 hover:shadow-md hover:shadow-blue-500/50"
                            }`}
                        >
                          <item.icon
                              className={`h-5 w-5 ${
                                  pathname === item.href ? "text-blue-600" : ""
                              }`}
                          />
                          <span
                              className={
                                pathname === item.href
                                    ? "font-semibold text-blue-600"
                                    : ""
                              }
                          >
                      {item.label}
                    </span>
                        </Link>
                      </li>
                  ))}
                </ul>
              </nav>
            </aside>
        )}


        <main className={`flex-grow p-4 ${shouldHideAsidebar ? "" : "ml-60"}`}>{children}</main>
      </div>
    </section>
  );
}
