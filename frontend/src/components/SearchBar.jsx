import {Input} from "@/components/ui/input.js";
import {Button} from "@/components/ui/button.js";
import {Search} from "lucide-react";
import FilterPopover from "@/components/FilterPopover.jsx";
import {observer} from "mobx-react-lite";
import {useContext, useState} from "react";
import {fetchProducts} from "@/http/productAPI.js";
import {Context} from "@/main.jsx";

function SearchBar() {
    const [search, setSearch] = useState("");
    const defaultMinPrice = 0;
    const defaultMaxPrice = 1000;
    const [priceRange, setPriceRange] = useState([defaultMinPrice, defaultMaxPrice]);
    const [selectedCategory, setSelectedCategory] = useState("");
    const [sortBy, setSortBy] = useState("");
    const {product} = useContext(Context);

    const handleSearch = () => {
        product.setCurrentPage(0);

        const filters = {
            page: product.currentPage,
            limit: product.limit,
        };

        if (selectedCategory) {
            filters.category = selectedCategory;
        }

        if (!(search.trim() === "")) {
            filters.search = search;
        }

        if (!(sortBy.trim() === "")) {
            filters.sortBy = sortBy;
        }

        if (!(priceRange[0] === defaultMinPrice && priceRange[1] === defaultMaxPrice)) {
            filters.priceRange = priceRange;
        }

        fetchProducts(filters).then((res) => {
            product.setProducts(res.data.content);
            product.setTotalPages(res.data.totalPages);
        });
    };

    return (
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
                    onClick={() => handleSearch()}
                    className="rounded-l-none"
                >
                    <Search className="w-4 h-4"/>
                </Button>
            </div>
            <FilterPopover
                categories={product.categories}
                handleSearch={handleSearch}
                priceRange={priceRange}
                setPriceRange={setPriceRange}
                selectedCategory={selectedCategory}
                setSelectedCategory={setSelectedCategory}
                sortBy={sortBy}
                setSortBy={setSortBy}
            />
        </div>
    )
}

export default observer(SearchBar);