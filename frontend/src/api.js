export const PREFIX = __APP__.env.prefix;

export async function getBucket(token) {
    let headers = new Headers();
    headers.append("Authorization", token);

    let requestOptions = {
        method: 'GET',
        headers: headers,
        redirect: 'follow'
    };

    const response = await fetch(PREFIX + "/bucket", requestOptions)
    if (response.status === 200) {
        return await response.json();
    } else {
        throw new Error(response.status);
    }
}

export async function deleteBucket(token) {
    let headers = new Headers();
    headers.append("Authorization", token);

    let requestOptions = {
        method: 'DELETE',
        headers: headers,
        redirect: 'follow'
    };

    const response = await fetch(PREFIX + "/bucket", requestOptions)
    if (response.status === 204) {
        return true
    } else {
        console.log(response)
        throw new Error(response.status);
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
    const response = await fetch(PREFIX + "/login", requestOptions)
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
    const response = await fetch(PREFIX + "/bucket", requestOptions);
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

export async function downloadFile(fileId, token, key) {
    let headers = new Headers();
    headers.append("Authorization", token);
    var requestOptions = {
    method: 'GET',
    headers: headers,
    redirect: 'follow'
    };

    const response = await fetch(PREFIX + "/download?fileId=" + fileId, requestOptions);

    if (response.status === 200) {
        return response.body
    } else {
        throw new Error(response.statusText)
    }
}

export async function deleteFile(fileId, token) {
    let headers = new Headers();
    headers.append("Authorization", token);
    var requestOptions = {
    method: 'DELETE',
    headers: headers,
    redirect: 'follow'
    };

    const response = await fetch(PREFIX + "/file/" + fileId, requestOptions);
    if (response.status === 204) {
        return true
    } else {
        throw new Error(response.statusText)
    }
}