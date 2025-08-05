import {Button} from "@/components/ui/button.js";
import {ChevronDown} from "lucide-react"
import {STORE_ROUTE} from "@/utils/consts.js";
import {useNavigate} from "react-router-dom";

function NavbarCatalogButton({categories}) {
    const navigate = useNavigate();

    return (

        <div className="relative group">
            <Button
                variant="ghost"
                className="flex items-center space-x-1 focus:outline-none focus:ring-0"
                onClick={() => navigate(STORE_ROUTE)}
            >
                <span>Catalog</span>
                <ChevronDown className="w-4 h-4 transition-transform duration-200 group-hover:rotate-180"/>
            </Button>

            <div className="absolute top-full left-0 pt-1 opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200 z-50">
                <div className="bg-background border rounded-md shadow-lg w-80 p-2">
                    <div className="grid grid-cols-2 gap-1">
                        {categories.map((category) => (
                            <div
                                key={category.name}
                                onClick={() => navigate(STORE_ROUTE, { state: { categoryId: category.id } })}
                                className="cursor-pointer hover:bg-accent hover:text-accent-foreground rounded-sm p-1 transition-colors duration-150 text-sm"
                            >
                                {category.name}
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </div>
    )
}

export default NavbarCatalogButton;