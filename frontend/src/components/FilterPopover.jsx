import {useState} from "react";
import {Button} from "@/components/ui/button";
import {Label} from "@/components/ui/label";
import {Popover, PopoverContent, PopoverTrigger} from "@/components/ui/popover";
import {Checkbox} from "@/components/ui/checkbox";
import {Slider} from "@/components/ui/slider";
import {Filter} from "lucide-react";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import {observer} from "mobx-react-lite";
import {RadioGroup, RadioGroupItem} from "@/components/ui/radio-group.js";

function FilterPopover({
    categories,
    handleSearch,
    defaultMaxPrice,
    priceRange,
    setPriceRange,
    selectedCategory,
    setSelectedCategory,
    sortBy,
    setSortBy,
}) {
    const [isOpen, setIsOpen] = useState(false);

    return (
        <div className="">
            <Popover open={isOpen} onOpenChange={setIsOpen}>
                <PopoverTrigger asChild>
                    <Button variant="outline" className="gap-2 bg-transparent">
                        <Filter className="w-4 h-4"/>
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
                                    max={defaultMaxPrice}
                                    min={0}
                                    step={1}
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
                            <RadioGroup
                                value={selectedCategory}
                                onValueChange={setSelectedCategory}
                                className="pr-2 max-h-48 overflow-y-auto"
                            >
                                <div className="flex items-center space-x-2">
                                    <RadioGroupItem value="" id="all"/>
                                    <Label htmlFor="all" className="text-sm font-normal cursor-pointer">
                                        All
                                    </Label>
                                </div>
                                {categories.map((category) => (
                                    <div key={category.id} className="flex items-center space-x-2">
                                        <RadioGroupItem value={category.id} id={category.id}/>
                                        <Label htmlFor={category.id} className="text-sm font-normal cursor-pointer">
                                            {category.name}
                                        </Label>
                                    </div>
                                ))}
                            </RadioGroup>
                        </div>

                        {/* Sort Chooser */}
                        <div className="space-y-3">
                            <Label className="text-sm font-medium">Sort By</Label>
                            <Select value={sortBy} onValueChange={setSortBy}>
                                <SelectTrigger>
                                    <SelectValue placeholder="Choose sorting option"/>
                                </SelectTrigger>
                                <SelectContent>
                                    <SelectItem value="createdAt,desc">Newest</SelectItem>
                                    <SelectItem value="price,asc">Price: Low to High</SelectItem>
                                    <SelectItem value="price,desc">Price: High to Low</SelectItem>
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
                                    setPriceRange([0, 1000]);
                                    setSelectedCategory([]);
                                    setSortBy("");
                                }}
                            >
                                Clear All
                            </Button>
                            <Button size="sm" className="flex-1" onClick={() => {
                                handleSearch();
                                setIsOpen(false);
                            }}>
                                Apply Filters
                            </Button>
                        </div>
                    </div>
                </PopoverContent>
            </Popover>
        </div>
    );
}

export default observer(FilterPopover);
