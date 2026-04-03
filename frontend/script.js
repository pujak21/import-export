document.getElementById("contactForm").addEventListener("submit", async function(e) {
    e.preventDefault();

    const contactData = {
        name: document.getElementById("name").value,
        email: document.getElementById("email").value,
        country: document.getElementById("country").value,
        serviceNeeded: document.getElementById("serviceNeeded").value,
        message: document.getElementById("message").value
    };

    try {
        const response = await fetch("https://import-export-da2fi.onrender.com/api/contact", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(contactData)
        });

        if (response.ok) {
            document.getElementById("responseMessage").innerText =
                "✅ Inquiry sent successfully! Our team will contact you soon.";
            document.getElementById("contactForm").reset();
        } else {
            document.getElementById("responseMessage").innerText =
                "❌ Something went wrong. Please try again.";
        }

    } catch (error) {
        document.getElementById("responseMessage").innerText =
            "⚠ Server not reachable. Make sure backend is running.";
    }
});
