
export async function getAllUsers() {

    const response = await fetch('/api/scans');
    return await response.json();
}

export async function createUser(data) {
    const response = await fetch(`/api/create-scan`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    })
    return await response.json();
}