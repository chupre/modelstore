import { ProductCard } from "@/components/ProductCard"
import {useState} from "react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Search } from "lucide-react"
import FilterPopover from "@/components/filter-popover";
import {
  Pagination,
  PaginationContent,
  PaginationEllipsis,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from "@/components/ui/pagination"

const products = [
  {
    title: "Sci-Fi Spaceship",
    image: "/images/spaceship.png",
    price: 19.99,
  },
  {
    title: "Medieval Castle",
    description: "Realistic low-poly medieval castle model.",
    image: "/images/castle.png",
    price: 14.99,
  },
  {
    title: "Cyberpunk Car",
    image: "/images/car.png",
    price: 11.50,
  },
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

export default function Store() {
  const [search, setSearch] = useState("");

  return (
    <div className="container py-8 max-w-7xl mx-auto px-4 pt-24">
      <div className="flex flex-col sm:flex-row sm:items-center mb-6 gap-4">
        <div className="flex w-full">
          <Input
            type="text"
            placeholder="Search products..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="rounded-r-none"
          />
          <Button
            type="submit"
            variant="default"
            size="icon"
            onClick={() => console.log("Searching for:", search)}
            className="rounded-l-none"
          >
            <Search className="w-4 h-4" />
          </Button>
        </div>
        <FilterPopover />
      </div>


      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
        {products
          .filter(p =>
            p.title.toLowerCase().includes(search.toLowerCase())
          )
          .map((p, i) => (
            <ProductCard key={i} {...p} />
          ))}
      </div>

      <Pagination className="pt-6">
        <PaginationContent>
          <PaginationItem>
            <PaginationPrevious href="#" />
          </PaginationItem>
          <PaginationItem>
            <PaginationLink href="#">1</PaginationLink>
          </PaginationItem>
          <PaginationItem>
            <PaginationEllipsis />
          </PaginationItem>
          <PaginationItem>
            <PaginationNext href="#" />
          </PaginationItem>
        </PaginationContent>
      </Pagination>
    </div>
  );
}