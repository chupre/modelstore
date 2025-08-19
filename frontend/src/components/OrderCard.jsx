import {Card, CardContent} from "@/components/ui/card.js";
import {Button} from "@/components/ui/button.js";
import {downloadProductModel} from "@/http/productAPI.js";
import errorToast from "@/utils/errorToast.jsx";
import {useNavigate} from "react-router-dom";
import {PRODUCT_ROUTE} from "@/utils/consts.js";

function OrderCard({order}) {
    const navigate = useNavigate();

    const getStatusColor = (status) => {
        switch (status) {
            case "PAID":
                return "bg-green-100 text-green-800 dark:bg-green-900/20 dark:text-green-400";
            case "PENDING":
                return "bg-yellow-100 text-yellow-800 dark:bg-yellow-900/20 dark:text-yellow-400";
            case "CANCELLED":
                return "bg-gray-100 text-gray-800 dark:bg-gray-900/20 dark:text-gray-400";
            case "FAILED":
                return "bg-red-100 text-red-800 dark:bg-red-900/20 dark:text-red-400";
            default:
                return "bg-gray-100 text-gray-800 dark:bg-gray-900/20 dark:text-gray-400";
        }
    };

    function handleDownload(id, title) {
        try {
            downloadProductModel(id).then((res) => {
                const url = window.URL.createObjectURL(new Blob([res.data]));
                const a = document.createElement("a");
                a.href = url;
                a.download = title + ".stl";
                document.body.appendChild(a);
                a.click();
                a.remove();
                window.URL.revokeObjectURL(url);
            });
        } catch (e) {
            errorToast(e);
        }
    }

    return (
        <Card key={order.id} className="border-border bg-card">
            <CardContent className="p-6">
                <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-4">
                    <div className="flex flex-col sm:flex-row sm:items-center gap-4">
                        <div>
                            <h3 className="font-semibold text-lg">ORDER-#{order.id}</h3>
                            <p className="text-sm text-muted-foreground">
                                Ordered on{" "}
                                {new Date(order.createdAt).toLocaleDateString("en-US", {
                                    year: "numeric",
                                    month: "long",
                                    day: "numeric",
                                })}
                            </p>
                        </div>
                        <span
                            className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-medium ${getStatusColor(order.status)}`}
                        >
                            {order.status}
                        </span>
                    </div>
                </div>

                <div className="space-y-3">
                    <h4 className="font-medium text-sm text-muted-foreground uppercase tracking-wide">
                        Order Items ({order.orderItems.length})
                    </h4>
                    <div className="grid gap-3">
                        {order.orderItems.map((item) => (
                            <div
                                key={item.product.id}
                                className="flex items-center gap-4 p-3 rounded-lg bg-muted/30 border border-border/50"
                            >
                                <img
                                    src={`${import.meta.env.VITE_API_URL}${item.product.previewImage}`}
                                    alt={item.product.title}
                                    className="w-16 h-16 rounded-md object-cover bg-muted cursor-pointer"
                                    onClick={() => navigate(PRODUCT_ROUTE + "/" + item.product.id)}
                                />

                                <div className="flex-1 min-w-0">
                                    <h5
                                        className="font-medium text-sm truncate cursor-pointer hover:underline"
                                        onClick={() => navigate(PRODUCT_ROUTE + "/" + item.product.id)}
                                    >
                                        {item.product.title}
                                    </h5>
                                </div>

                                <Button
                                    size="sm"
                                    variant="outline"
                                    onClick={() => handleDownload(item.product.id, item.product.title)}
                                    disabled={order.status !== "PAID"}
                                    className="gap-2 flex-shrink-0"
                                >
                                    <svg
                                        className="h-4 w-4"
                                        fill="none"
                                        stroke="currentColor"
                                        viewBox="0 0 24 24"
                                    >
                                        <path
                                            strokeLinecap="round"
                                            strokeLinejoin="round"
                                            strokeWidth={2}
                                            d="M12 10v6m0 0l-3-3m3 3l3-3m2 8H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"
                                        />
                                    </svg>
                                    Download
                                </Button>
                            </div>
                        ))}
                    </div>
                </div>
            </CardContent>
        </Card>
    );
}

export default OrderCard;
