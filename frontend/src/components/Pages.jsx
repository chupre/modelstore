import {
    Pagination,
    PaginationContent, PaginationEllipsis,
    PaginationItem,
    PaginationLink, PaginationNext,
    PaginationPrevious
} from "@/components/ui/pagination.js";
import {observer} from "mobx-react-lite";
import {useContext, useEffect} from "react";
import {Context} from "@/main.jsx";

function Pages() {
    const {product} = useContext(Context);

    const goToPage = (page) => {
        product.setCurrentPage(page);
    };

    useEffect(() => {
        window.scrollTo({ top: 0 });
    }, [product.currentPage]);

    return (
        <Pagination className="pt-6">
            <PaginationContent>
                <PaginationItem>
                    <PaginationPrevious
                        onClick={() => product.currentPage > 0 && goToPage(product.currentPage - 1)}
                        className="cursor-pointer"
                    />
                </PaginationItem>

                {[...Array(product.totalPages)].map((_, i) => (
                    <PaginationItem key={i}>
                        <PaginationLink
                            isActive={i === product.currentPage}
                            onClick={() => goToPage(i)}
                            className="cursor-pointer"
                        >
                            {i + 1}
                        </PaginationLink>
                    </PaginationItem>
                ))}

                <PaginationItem>
                    <PaginationNext
                        onClick={() => product.currentPage < product.totalPages - 1 && goToPage(product.currentPage + 1)}
                        className="cursor-pointer"
                    />
                </PaginationItem>
            </PaginationContent>
        </Pagination>
    )
}

export default observer(Pages);