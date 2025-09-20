document.getElementById("signinForm").addEventListener("submit", async function(e) {
    e.preventDefault();

    const user = {
        name: document.getElementById("name").value,
        email: document.getElementById("email").value
    };

    const response = await fetch("/users", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(user)
    });

    const savedUser = await response.json();

    if (savedUser && savedUser.id) {
        window.location.href = `/dashboard.html?id=${savedUser.id}`;
    } else {
        alert("Sign in failed");
    }
});