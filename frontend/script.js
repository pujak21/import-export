document.getElementById("contactForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const contactData = {
        name: document.getElementById("name").value,
        email: document.getElementById("email").value,
        country: document.getElementById("country").value,
        serviceNeeded: document.getElementById("serviceNeeded").value,
        message: document.getElementById("message").value
    };

    fetch("http://localhost:8080/api/contact", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(contactData)
    })
    .then(response => response.json())
    .then(data => {
        alert("Contact saved successfully!");
        console.log(data);
    })
    .catch(error => {
        console.error("Error:", error);
    });
});
