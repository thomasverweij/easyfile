const URL = "http://localhost:8080"

export async function getBucket(bucketId, token) {
    let headers = new Headers();
    headers.append("Authorization", token);

    let requestOptions = {
        method: 'GET',
        headers: headers,
        redirect: 'follow'
    };

    const response = await fetch(URL + "/bucket", requestOptions)
    if (response.status === 200) {
        return await response.json();
    } else {
        throw new Error(response.statusText);
    }
}

export async function getToken(bucketId, password) {
    let headers = new Headers();
    headers.append("Content-Type", "application/json");
    let raw = JSON.stringify({"id":bucketId,"password":password});
    let requestOptions = {
        method: 'POST',
        headers: headers,
        body: raw,
        redirect: 'follow'
    };
    const response = await fetch(URL + "/login", requestOptions)
    if (response.status === 200) {
        let token = response.headers.get("Authorization")
        return {status: response.status, token: token}
    } else if (response.status === 401) {
        return {status: response.status, token: ""}
    } else {
        throw new Error(response.statusText);
    }
}

export async function createBucket(password) {
    let headers = new Headers();
    headers.append("Content-Type", "application/json");
    let raw = JSON.stringify({"password":password});
    var requestOptions = {
        method: 'POST',
        headers: headers,
        body: raw,
        redirect: 'follow'
    };
    const response = await fetch(URL+ "/bucket", requestOptions);
    if (response.status === 200) {
        return await response.json()
    } else {
        throw new Error(response.statusText)
    }
}

export async function createBucketAndLogin(password) {
    let bucket = await createBucket(password)
    let token = await getToken(bucket.id, password)
    console.log(token)
    return {id: bucket.id, token: token.token}
}

export async function uploadFile(file, token) {
    let headers = new Headers();
    headers.append("Authorization", token);
    let formdata = new FormData();
    formdata.append("file", file, file.name);

    let requestOptions = {
        method: 'POST',
        headers: headers,
        body: formdata,
        redirect: 'follow'
    };

    const response = await fetch("http://localhost:8080/file", requestOptions);
    if (response.status === 200) {
        return await response.json()
    } else {
        throw new Error(response.statusText)
    }

}