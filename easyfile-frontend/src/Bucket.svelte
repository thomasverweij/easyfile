<script>
import { onMount } from 'svelte';

    import { downloadFile, getBucket, uploadFile } from './api.js'
    import { timeSince } from './utils.js'
    export let bucketId;
    export let token;

    let files;
    let uploading;
    let bucketData;
    let currentFileMenu;

    $: if(files) upload()

    let upload = async () => {
        uploading = true;
        let res = await uploadFile(files[0], token)
            .catch(() => alert("could not upload file"))
            .finally(() => uploading = false);
        if (res.id) {
            getBucket(token).then((r) => { bucketData = r});
        }
    } 

    let download = async (e) => {
        e.preventDefault();
        let id = e.currentTarget.dataset.id;
        let filename = e.currentTarget.dataset.filename;
        let blob = await downloadFile(id, token)
            .catch(() => alert("could not download file"))
        let elemx = window.document.createElement('a');
        elemx.href = window.URL.createObjectURL(blob);
        elemx.download = filename;
        elemx.style.display = 'none';
        document.body.appendChild(elemx);
        elemx.click();
        document.body.removeChild(elemx);
    }

    let logout = () => {
        bucketData = undefined;
        token = undefined;
        bucketId = undefined;
        files = undefined;
        window.location = window.location.hash.split('#')[0];
        localStorage.removeItem('token');
    }

    let selectFile = (e) =>  {
        e.preventDefault();
        document.getElementById("fileInput").click()
    } 

    let copyLink = (e) => {
        e.preventDefault()
        var tempInput = document.createElement("input");
        tempInput.style = "position: absolute; left: -1000px; top: -1000px";
        tempInput.value = window.location.href;
        document.body.appendChild(tempInput);
        tempInput.select();
        document.execCommand("copy");
        document.body.removeChild(tempInput);
        alert("Bucket link copied to clipboard")
    }

    let deleteBucket = () => {
        if (confirm("This will also delete the bucket contents. Proceed?")){
            alert("bucket deleted")
        }
    }

    let dropHandler = (e) => {
        e.preventDefault();
        if (e.dataTransfer.items) {
            files = [e.dataTransfer.items[0].getAsFile()]
        } else {
            files = e.dataTransfer.files
        }
    }

    let toggleMenu = (id) => {
        if(currentFileMenu === id) {currentFileMenu = undefined}
        else {currentFileMenu = id}
    }

    onMount(() => {
        getBucket(token).then((r) => {
            bucketData = r;
            window.location.hash = r.id;
        }).catch(() => alert("somehting went wrong"));
        document.body.ondragover = (e) => {e.preventDefault()};
        document.body.ondrop = dropHandler;
    });

</script>

{#if bucketData}
    <div class="options">
        <div class="options-item"><a href="/" on:click={selectFile}>Add file</a></div>
        <div class="options-item"><a href="/" on:click={copyLink}>Copy bucket link</a></div>
        <div class="options-item"><a href="/" on:click={deleteBucket}>Delete bucket</a></div>
        <div class="options-item"><a href="/" on:click={logout}>Logout</a></div>
    </div>
    <div class="files">
    <p>
        <span class:invisible={!uploading}>uploading file...</span>        
    </p>
    <p></p>
    <table class:invisible={bucketData.files.length == 0}> 
        <thead>
            <tr>
            <th class="rownumber">#</th>
            <th>File</th>
            <th class="uploaded">Uploaded</th>
            </tr>
        </thead>
        <tbody>
            {#each bucketData.files as file, i}
            <tr>
            <th scope="row">{i + 1}</th>
            <td><a href="/" data-id="{file.id}" title="{file.fileName}" on:click|preventDefault="{() => toggleMenu(file.id)}">{file.fileName}</a></td>
            <td>{timeSince(file.createdDate)} ago</td>
            </tr>
            <tr class:activerow={currentFileMenu != file.id}>
                <td></td>
                <td colspan="2" class="filemenu">
                    <a href="/" data-id="{file.id}" data-filename="{file.fileName}" on:click={download}>Download</a>
                    &nbsp;&nbsp;
                    <a href="/" data-id="{file.id}">Delete</a>
                </td>
            </tr>
            {/each}
        </tbody>
    </table>
    <p class:invisible={bucketData.files.length != 0}><i>Drag and drop your file here, or click 'Add file' to upload your file.</i></p>
    </div>
    <input bind:files type="file" class="invisible" id="fileInput" name="fileInput" />
{:else}
    <p>loading bucket...</p>
{/if}

<style>
    .options {
        display: flex;
        flex-direction: row;
    }

    .options-item {
        flex-grow: 1;
        text-align: center;
    }

    .invisible {
        visibility: hidden;
    }

    .activerow {
        display: none;
        background: white;
    }

    .filemenu {
        padding-bottom: 20px;
    }

    .files {
        text-align: center;
    }


    #fileInput {
        overflow: hidden;
    }

    table {
        width: 100%;
        text-align: left;
        table-layout: fixed;
    }

    td, th {
        /* max-width: 400px; */
        overflow: hidden;
        padding-left: 10px;
        text-overflow: ellipsis;
        white-space: nowrap;
    }

    tr {
        height: 30px;
        animation: slide-down .1s ease-out;
    }

    @keyframes slide-down {
        0% { opacity: 0; -webkit-transform: translateY(-50%); }   
        100% { opacity: 1; -webkit-transform: translateY(0); }
    }

    .rownumber {
        width: 30px;
    }

    .uploaded {
        width: 120px;
    }

    a {
  		color:inherit;
	}
	 
</style>
