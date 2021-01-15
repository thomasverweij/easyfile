<script>
    import { getBucket, uploadFile } from './api.js'
    import { timeSince } from './utils.js'
    export let bucketId;
    export let token;

    let files;
    let bucketData = getBucket(bucketId, token);

    $: if(bucketData) {
        window.location.hash = bucketId;
    }

    async function upload() {
        let response = await uploadFile(files[0], token)
        if (response.id) {
            bucketData = getBucket(bucketId, token);
        }
    } 

    function logout() {
        bucketData = undefined;
        token = undefined;
        bucketId = undefined;
        window.location = window.location.hash.split('#')[0];
    }

</script>

{#await bucketData}
    loading...
{:then result}
    <p>Current bucket: <code>{result.id}</code> <button on:click={logout}>logout</button></p>
    <p id="uploadfield"><input bind:files type="file" id="fileInput" name="fileInput" /> <button on:click={upload}>Upload</button></p>
    <p></p>
    <table class:invisible={result.files.length == 0}> 
        <thead>
            <tr>
            <th>#</th>
            <th>Filename</th>
            <th>Uploaded</th>
            </tr>
        </thead>
        <tbody>
            {#each result.files as file, i}
            <tr>
            <th scope="row">{i + 1}</th>
            <td>{file.fileName}</td>
            <td>{timeSince(file.createdDate)} ago</td>
            </tr>
            {/each}
        </tbody>
    </table>
{:catch error}
	error loading bucket: {error}
{/await}

<style>
    button {
        background: white;
    }

    .invisible {
        visibility: hidden;
    }

    #uploadfield {
        margin-top: 50px;
        margin-bottom: 50px;
    }

    #fileInput {
        overflow: hidden;
    }

    table {
        width: 100%;
    }

    td {
        max-width: 400px;
        overflow: hidden;
        padding-left: 10px;
        text-overflow: ellipsis;
        white-space: nowrap;
    }

    tr {
        height: 30px;
    }
</style>
