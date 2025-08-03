import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Label } from "@/components/ui/label"
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover"
import { Checkbox } from "@/components/ui/checkbox"
import { Slider } from "@/components/ui/slider"
import { Filter } from "lucide-react"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"

export default function Component() {
  const [priceRange, setPriceRange] = useState([0, 1000])
  const [selectedCategories, setSelectedCategories] = useState<string[]>([])
  const [sortBy, setSortBy] = useState("")
  const [isOpen, setIsOpen] = useState(false)

  return (
    <div className="p-8">
      <Popover open={isOpen} onOpenChange={setIsOpen}>
        <PopoverTrigger asChild>
          <Button variant="outline" className="gap-2 bg-transparent">
            <Filter className="w-4 h-4" />
            Filters & Sort
          </Button>
        </PopoverTrigger>
        <PopoverContent className="w-80 p-6" align="start">
          <div className="space-y-6">
            {/* Price Slider */}
            <div className="space-y-3">
              <Label className="text-sm font-medium">Price Range</Label>
              <div className="px-2">
                <Slider
                  value={priceRange}
                  onValueChange={setPriceRange}
                  max={1000}
                  min={0}
                  step={10}
                  className="w-full"
                />
              </div>
              <div className="flex justify-between text-sm text-muted-foreground">
                <span>${priceRange[0]}</span>
                <span>${priceRange[1]}</span>
              </div>
            </div>

            {/* Category Chooser */}
            <div className="space-y-3">
              <Label className="text-sm font-medium">Categories</Label>
              <div className="max-h-48 overflow-y-auto space-y-2 pr-2">
                {[
                  { id: "electronics", label: "Electronics" },
                  { id: "clothing", label: "Clothing & Fashion" },
                  { id: "books", label: "Books & Media" },
                  { id: "home", label: "Home & Garden" },
                  { id: "sports", label: "Sports & Outdoors" },
                  { id: "beauty", label: "Beauty & Personal Care" },
                  { id: "automotive", label: "Automotive" },
                  { id: "toys", label: "Toys & Games" },
                  { id: "health", label: "Health & Wellness" },
                  { id: "food", label: "Food & Beverages" },
                  { id: "office", label: "Office Supplies" },
                  { id: "pet", label: "Pet Supplies" },
                  { id: "jewelry", label: "Jewelry & Accessories" },
                  { id: "music", label: "Musical Instruments" },
                ].map((category) => (
                  <div key={category.id} className="flex items-center space-x-2">
                    <Checkbox
                      id={category.id}
                      checked={selectedCategories.includes(category.id)}
                      onCheckedChange={(checked) => {
                        if (checked) {
                          setSelectedCategories([...selectedCategories, category.id])
                        } else {
                          setSelectedCategories(selectedCategories.filter((id) => id !== category.id))
                        }
                      }}
                    />
                    <Label htmlFor={category.id} className="text-sm font-normal cursor-pointer">
                      {category.label}
                    </Label>
                  </div>
                ))}
              </div>
            </div>

            {/* Sort Chooser */}
            <div className="space-y-3">
              <Label className="text-sm font-medium">Sort By</Label>
              <Select value={sortBy} onValueChange={setSortBy}>
                <SelectTrigger>
                  <SelectValue placeholder="Choose sorting option" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="featured">Featured</SelectItem>
                  <SelectItem value="newest">Newest</SelectItem>
                  <SelectItem value="price-low">Price: Low to High</SelectItem>
                  <SelectItem value="price-high">Price: High to Low</SelectItem>
                  <SelectItem value="rating">Customer Rating</SelectItem>
                  <SelectItem value="bestseller">Best Seller</SelectItem>
                </SelectContent>
              </Select>
            </div>

            {/* Action Buttons */}
            <div className="flex gap-2 pt-2">
              <Button
                variant="outline"
                size="sm"
                className="flex-1 bg-transparent"
                onClick={() => {
                  setPriceRange([0, 1000])
                  setSelectedCategories([])
                  setSortBy("")
                }}
              >
                Clear All
              </Button>
              <Button size="sm" className="flex-1" onClick={() => setIsOpen(false)}>
                Apply Filters
              </Button>
            </div>
          </div>
        </PopoverContent>
      </Popover>
    </div>
  )
}
