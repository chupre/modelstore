import {
    Pagination,
    PaginationContent,
    PaginationItem,
    PaginationLink,
    PaginationNext,
    PaginationPrevious
} from "@/components/ui/pagination.js";
import {observer} from "mobx-react-lite";
import {useEffect} from "react";

function Pages({totalPages, currentPage, setCurrentPage}) {
    const goToPage = (page) => {
        setCurrentPage(page);
    };

    useEffect(() => {
        window.scrollTo({ top: 0 });
    }, [currentPage]);

    return (
        <Pagination className="pt-6 bg-0">
            <PaginationContent>
                <PaginationItem>
                    <PaginationPrevious
                        onClick={() => currentPage > 0 && goToPage(currentPage - 1)}
                        className="cursor-pointer"
                    />
                </PaginationItem>

                {[...Array(totalPages)].map((_, i) => (
                    <PaginationItem key={i}>
                        <PaginationLink
                            isActive={i === currentPage}
                            onClick={() => goToPage(i)}
                            className="cursor-pointer"
                        >
                            {i + 1}
                        </PaginationLink>
                    </PaginationItem>
                ))}

                <PaginationItem>
                    <PaginationNext
                        onClick={() => currentPage < totalPages - 1 && goToPage(currentPage + 1)}
                        className="cursor-pointer"
                    />
                </PaginationItem>
            </PaginationContent>
        </Pagination>
    )
}

export default observer(Pages);