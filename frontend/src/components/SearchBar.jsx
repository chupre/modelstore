import {Input} from "@/components/ui/input.js";
import {Button} from "@/components/ui/button.js";
import {Search} from "lucide-react";
import FilterPopover from "@/components/FilterPopover.jsx";
import {observer} from "mobx-react-lite";
import {useContext} from "react";
import {Context} from "@/main.jsx";

function SearchBar({className}) {
    const defaultMaxPrice = 200;
    const {product} = useContext(Context);

    const handleSearch = () => {
        product.setCurrentPage(0);
        product.fetchProducts();
    };

    return (
        <div className={`flex flex-col sm:flex-row sm:items-center mb-6 gap-4 ${className}`}>
            <div className="bg-background/40 backdrop-blur-lg flex w-full overflow-hidden rounded-md border border-input focus-within:border-ring focus-within:ring-1 focus-within:ring-ring drop-shadow-2xl">
                <Input
                    type="text"
                    placeholder="Search products..."
                    value={product.search}
                    onChange={(e) => product.setSearch(e.target.value)}
                    className="border-0 rounded-none"
                />
                <Button
                    type="submit"
                    variant="default"
                    size="icon"
                    onClick={() => handleSearch()}
                    className="rounded-none border-0"
                >
                    <Search className="w-4 h-4"/>
                </Button>
            </div>
            <FilterPopover
                handleSearch={handleSearch}
                defaultMaxPrice={defaultMaxPrice}
            />
        </div>
    )
}

export default observer(SearchBar);