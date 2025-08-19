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
    } else if (e.response.status === 403) {
        toast.error(`Error`, {
            description: "Access forbidden. Make sure your account is verified"
        })
    }
    else {
        toast.error("Unexpected error", {
            description: "Something went wrong. Please try again.",
        });
    }
}