import type { Metadata } from "next";
import { SharedLayout } from "@/app/shared/layout";
import "./globals.css";

export const metadata: Metadata = {
  title: "New Bank",
  description: "Digital bank in your hands",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body className="bg-white text-black">
        <SharedLayout>{children}</SharedLayout>
      </body>
    </html>
  );
}
