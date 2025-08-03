import { Button } from "@/components/ui/button"
import { ProductCard } from "@/components/ProductCard"
import { useNavigate } from "react-router-dom"
import { STORE_ROUTE } from "../utils/consts"

const featuredProducts = [
  {
    title: "Sci-Fi Spaceship",
    image: "/images/spaceship.png",
    price: 19.99,
  },
  {
    title: "Medieval Castle",
    image: "/images/castle.png",
    price: 14.99,
  },
  {
    title: "Cyberpunk Car",
    image: "/images/car.png",
    price: 11.50,
  },
]

export default function Home() {
  const navigate = useNavigate()

  return (
    <div className="max-w-7xl mx-auto px-4 pt-20">
      {/* Hero section */}
      <section className="bg-background pt-10 pb-10 text-center px-4">
        <h1 className="text-4xl sm:text-5xl font-bold mb-4">Discover 3D Assets</h1>
        <p className="text-lg text-muted-foreground mb-6">
          Browse high-quality sci-fi and fantasy models for your games, renders, or 3D printing.
        </p>
        <Button className="text-lg px-6 py-3" size="lg" onClick={() => navigate(STORE_ROUTE)}>
          Explore Store
        </Button>
      </section>

      {/* Featured section */}
      <section className="container">
        <h2 className="text-2xl font-bold mb-6">Featured Models</h2>
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
          {featuredProducts.map((p, i) => (
            <ProductCard key={i} {...p} />
          ))}
        </div>
      </section>
    </div>
  )
}
