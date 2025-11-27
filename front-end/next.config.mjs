// front-end/next.config.mjs

const isProd = process.env.NODE_ENV === "production";

/** @type {import('next').NextConfig} */
const nextConfig = {
  // Gera HTML estático (necessário pro GitHub Pages)
  output: "export",

  // Caminho quando estiver publicado em:
  // https://imfeear.github.io/bank-account-transactions-payment
  basePath: isProd ? "/bank-account-transactions-payment" : "",
  assetPrefix: isProd ? "/bank-account-transactions-payment/" : "",

  // Imagens sem otimizador (GitHub Pages não suporta o otimizador do Next)
  images: {
    unoptimized: true,
  },

  // ⚠ Aqui é o pulo do gato: ignora ESLint só no build
  eslint: {
    ignoreDuringBuilds: true,
  },
};

export default nextConfig;
