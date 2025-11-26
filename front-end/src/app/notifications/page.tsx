"use client";

import { useState, useEffect } from "react";
import { Card, CardHeader, CardContent } from "@/components/ui/card";
import { Bell } from "lucide-react";
import { fetchNotifications, clearNotifications } from "@/lib/utils";
import { InfoCircledIcon } from "@radix-ui/react-icons";

interface Notification {
  id: number;
  title: string;
  message: string;
  timestamp: string;
}

export default function Notifications() {
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [loading, setLoading] = useState(true);

  const fetchNotificationsData = async () => {
    try {
      const notificationsData = await fetchNotifications();
      setNotifications(notificationsData);
    } catch (error) {
      console.error("Erro ao carregar notificações:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchNotificationsData();
  }, []);

  const clearNotificationsData = async () => {
    try {
      await clearNotifications();
      setNotifications([]);
    } catch (error) {
      console.error("Erro ao limpar notificações:", error);
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (dateString: string | number | Date): string => {
    const date = new Date(dateString);
    if (isNaN(date.getTime())) return "Data inválida"; // Adicionar verificação de data inválida
    const day = date.getDate().toString().padStart(2, "0");
    const month = (date.getMonth() + 1).toString().padStart(2, "0");
    const year = date.getFullYear();
    const hours = date.getHours().toString().padStart(2, "0");
    const minutes = date.getMinutes().toString().padStart(2, "0");

    return `${day}/${month}/${year} ${hours}:${minutes}`;
  };

  return (
    <main className="flex-grow container mx-auto p-4 md:p-6 lg:p-8 relative">
      <div className="flex flex-col md:flex-row gap-6">
        <section className="md:w-3/4">
          <h2 className="text-2xl font-semibold mb-6">Suas Notificações</h2>
          <div className="grid grid-cols-1 gap-6">
            <Card className="w-full min-h-64 my-0 mx-auto lg:m-0">
              <CardHeader>
                <div className="flex items-center gap-2">
                  <Bell className="w-6 h-6" />
                  Notificações
                </div>
                <div className="flex items-center gap-2">
                  <button
                    onClick={clearNotificationsData}
                    className="text-sm text-gray-500 hover:text-gray-700"
                  >
                    Limpar todas
                  </button>
                </div>
              </CardHeader>
              <CardContent>
                {loading ? (
                  <div className="flex justify-center items-center h-32">
                    <p>Carregando notificações...</p>
                  </div>
                ) : notifications.length === 0 ? (
                  <div className="flex justify-center items-center h-32">
                    <p>Nenhuma notificação encontrada</p>
                  </div>
                ) : (
                  <div className="space-y-4">
                    {notifications.map((notification) => (
                      <div
                        key={notification.id}
                        className="flex items-center justify-between p-4 border rounded-lg hover:bg-gray-50"
                      >
                        <div className="flex items-center gap-4">
                          <InfoCircledIcon className="w-6 h-6" />
                          <div>
                            <p className="font-semibold">
                              {notification.title}
                            </p>
                            <p className="text-sm text-gray-500">
                              {formatDate(notification.timestamp)}
                            </p>
                          </div>
                        </div>
                        <p className="text-gray-700">{notification.message}</p>
                      </div>
                    ))}
                  </div>
                )}
              </CardContent>
            </Card>
          </div>
        </section>
      </div>
    </main>
  );
}
