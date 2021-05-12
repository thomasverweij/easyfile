const API = 'api'

export async function getBucket(token) {
    let headers = new Headers();
    headers.append("Authorization", token);

    let requestOptions = {
        method: 'GET',
        headers: headers,
        redirect: 'follow'
    };

    const response = await fetch(API + "/bucket", requestOptions)
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
    const response = await fetch(API + "/login", requestOptions)
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
    const response = await fetch(API + "/bucket", requestOptions);
    if (response.status === 200) {
        return await response.json()
    } else {
        throw new Error(response.statusText)
    }
}

export async function createBucketAndLogin(password) {
    let bucket = await createBucket(password)
    let token = await getToken(bucket.id, password)
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

    const response = await fetch(API + "/file", requestOptions);
    if (response.status === 200) {
        return await response.json()
    } else {
        throw new Error(response.statusText)
    }
}
export async function downloadFile(fileId, token) {
    let headers = new Headers();
    headers.append("Authorization", token);
    var requestOptions = {
    method: 'GET',
    headers: headers,
    redirect: 'follow'
    };

    const response = await fetch(API + "/download?fileId=" + fileId, requestOptions);
    if (response.status === 200) {
        return await response.blob()
    } else {
        throw new Error(response.statusText)
    }
}