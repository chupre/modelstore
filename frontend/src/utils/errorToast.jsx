import {toast} from "sonner";

export default function errorToast(e) {
    const responseData = e?.response?.data;
    if (responseData && typeof responseData === "object") {
        const messages = Object.values(responseData);

        toast.error('Error', {
            description: (
                messages.map((msg, i) => (
                    <p key={i}>{msg}</p>
                ))
            ),
        });
    } else {
        toast.error("Unexpected error", {
            description: "Something went wrong. Please try again.",
        });
    }
}