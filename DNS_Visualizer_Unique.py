import tkinter as tk
from tkinter import messagebox

# ----------------- DNS GUI Simulation -----------------

class DNSGui:
    def __init__(self, root):
        self.root = root
        self.root.title("DNS Resolution Simulator")
        self.root.geometry("700x500")
        self.root.config(bg="#1e1e2e")

        self.cache_stored = False  # flag for cache

        # Title
        tk.Label(root, text="DNS Resolution Visualizer", font=("Arial", 20, "bold"), bg="#1e1e2e", fg="white").pack(pady=10)

        # Domain input box
        input_frame = tk.Frame(root, bg="#1e1e2e")
        input_frame.pack()
        tk.Label(input_frame, text="Enter Domain:", font=("Arial", 12), bg="#1e1e2e", fg="white").pack(side="left", padx=5)
        self.domain_entry = tk.Entry(input_frame, width=30, font=("Arial", 12))
        self.domain_entry.pack(side="left", padx=5)

        # Query type selection
        self.query_type = tk.StringVar(value="Recursive")
        tk.Radiobutton(input_frame, text="Recursive", variable=self.query_type, value="Recursive",
                       bg="#1e1e2e", fg="white", selectcolor="#333333").pack(side="left", padx=5)
        tk.Radiobutton(input_frame, text="Iterative", variable=self.query_type, value="Iterative",
                       bg="#1e1e2e", fg="white", selectcolor="#333333").pack(side="left", padx=5)

        # Canvas for servers
        self.canvas = tk.Canvas(root, width=650, height=150, bg="#2d2d44", highlightthickness=0)
        self.canvas.pack(pady=15)
        self.draw_servers()

        # Output Log
        self.output = tk.Text(root, height=10, width=80, bg="#2d2d44", fg="white", font=("Arial", 11), wrap="word")
        self.output.pack(pady=10)

        # Buttons
        btn_frame = tk.Frame(root, bg="#1e1e2e")
        btn_frame.pack()
        tk.Button(btn_frame, text="Start Resolution ▶", bg="#4CAF50", fg="white", font=("Arial", 12, "bold"),
                  command=self.start_resolution).pack(side="left", padx=10)
        tk.Button(btn_frame, text="Clear & Restart 🔄", bg="#FF9800", fg="white", font=("Arial", 12, "bold"),
                  command=self.clear_all).pack(side="left", padx=10)

    def draw_servers(self):
        # Draw server boxes
        self.servers = {
            "Host": self.canvas.create_rectangle(30, 50, 110, 100, fill="gray", outline="white"),
            "Local": self.canvas.create_rectangle(140, 50, 240, 100, fill="gray", outline="white"),
            "Root": self.canvas.create_rectangle(270, 50, 350, 100, fill="gray", outline="white"),
            "TLD": self.canvas.create_rectangle(380, 50, 460, 100, fill="gray", outline="white"),
            "Auth": self.canvas.create_rectangle(490, 50, 600, 100, fill="gray", outline="white"),
        }
        labels = ["Host", "Local DNS", "Root DNS", "TLD DNS", "Authoritative"]
        x = 70
        for lbl in labels:
            self.canvas.create_text(x, 110, text=lbl, fill="white", font=("Arial", 10, "bold"))
            x += 110

    def highlight_server(self, server):
        # Reset colors first
        for s in self.servers:
            self.canvas.itemconfig(self.servers[s], fill="gray")
        self.canvas.itemconfig(self.servers[server], fill="#4CAF50")  # highlight green

    def start_resolution(self):
        domain = self.domain_entry.get()
        if not domain:
            messagebox.showwarning("Input Required", "Please enter a domain name!")
            return

        self.output.delete(1.0, tk.END)
        self.output.insert(tk.END, f"Resolving domain: {domain}\n")
        self.output.insert(tk.END, f"Query Type: {self.query_type.get()}\n\n")

        if self.cache_stored:
            self.output.insert(tk.END, "✅ Cache Hit! Local DNS returned IP instantly.\n")
            self.highlight_server("Local")
            return

        steps = [
            ("Host", f"Requesting Host sends query for {domain} to Local DNS"),
            ("Local", "Local DNS checking cache... Cache Miss!"),
            ("Root", "Local DNS contacts Root DNS to find TLD server"),
            ("TLD", "TLD DNS provides address of Authoritative DNS"),
            ("Auth", f"Authoritative DNS replies with IP Address for {domain}"),
            ("Local", "Local DNS stores result in cache for future queries"),
            ("Host", "Reply sent back to Requesting Host ✅")
        ]

        if self.query_type.get() == "Recursive":
            for server, text in steps:
                self.highlight_server(server)
                self.root.update()
                self.output.insert(tk.END, text + "\n")
                self.root.after(700)
        else:
            # Iterative query (Local DNS just gives referrals)
            self.output.insert(tk.END, "Iterative Query Selected: Resolver gets referral at each step.\n")
            for server, text in steps:
                self.highlight_server(server)
                self.root.update()
                self.output.insert(tk.END, "[Referral] " + text + "\n")
                self.root.after(700)

        self.cache_stored = True

    def clear_all(self):
        self.output.delete(1.0, tk.END)
        self.domain_entry.delete(0, tk.END)
        self.cache_stored = False
        for s in self.servers:
            self.canvas.itemconfig(self.servers[s], fill="gray")


# ----------------- Main Program -----------------
if __name__ == "__main__":
    root = tk.Tk()
    gui = DNSGui(root)
    root.mainloop()
