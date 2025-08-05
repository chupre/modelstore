import {ThemeProvider} from "@/components/ui/theme-provider.js";
import {Spinner} from "@/components/ui/shadcn-io/spinner/index.js";

export default function Loading() {
    return (
        <ThemeProvider defaultTheme="dark" storageKey="vite-ui-theme">
            <div className="w-screen h-screen flex items-center justify-center">
                <Spinner />
            </div>
        </ThemeProvider>
    )
}