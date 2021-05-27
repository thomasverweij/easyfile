<script>        
    import { notify, timeSince, decryptFile, getSalt } from './utils.js'
    import { deleteFile, downloadFile } from './api.js';
    import { tokenStore, keyStore } from './store.js'
    import * as streamsaver from 'streamsaver'

    export let bucketData;
    export let uploading;
    let downloading
    let currentFileMenu;

    let download = async (e) => {
        try {
            let id = e.currentTarget.dataset.id;
            let filename = e.currentTarget.dataset.filename;
            downloading = id;
            let stream = await downloadFile(id, $tokenStore)
            let decryptedFile = await decryptFile(stream, $keyStore, getSalt(bucketData.id))
            const fileStream = streamsaver.createWriteStream(filename)
            decryptedFile.pipeTo(fileStream)
        } catch (e) {
            console.log(e)
            notify("could not download file")
        }
        downloading = false
    }

    let deletef = (e) => {
        let fileId = e.target.dataset.id
        deleteFile(fileId, $tokenStore).then(() => {
            bucketData.files = bucketData.files.filter((f) => (f.id != fileId))
            notify("File deleted")
        }).catch(() => {
            notify("Unable to delete file")
        })
    }
    let toggleMenu = (id) => {
        if(currentFileMenu === id) {currentFileMenu = undefined}
        else {currentFileMenu = id}
    }

    $: hideFilelist = bucketData.files.length == 0 && !uploading

</script>
<div id="fileList">
{#if hideFilelist}
    <p class="droparea">
        <i>Drag and drop your file here, or click 'Add file' to upload your file.</i>
    </p>
{:else}
    <table> 
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
                <td class="filemenu">
                    <a href="/" id="{file.id}" data-id="{file.id}" data-filename="{file.fileName}" on:click|preventDefault={download}>
                        {#if downloading}
                        <div class="loader"></div>
                        {/if}
                        Download
                    </a>
                    &nbsp;&nbsp;
                    <a href="/" data-id="{file.id}" on:click|preventDefault={deletef}>Delete</a>
                </td>
            </tr>
            {/each}
            {#if uploading}
            <tr>
                <th scope="row"><div class="loader"></div></th>
                <td>{uploading}</td>
                <td></td>
            </tr>
            {/if}
        </tbody>
    </table>
{/if}
</div>

<style>
    #fileList {
        margin-top: 20px;
        text-align: center;
    }

    .activerow {
        display: none;
        background: white;
    }

    .filemenu {
        padding-bottom: 20px;
    }

    table {
        width: 90%;
        text-align: left;
        table-layout: fixed;
    }

    td, th {
        overflow: hidden;
        padding-left: 10px;
        overflow-wrap: break-word;
        word-wrap: break-word;
    }

    tr {
        height: 30px;
        animation: slide-down .1s ease-out;
    }

    @media (max-width: 500px){
        tr th:nth-child(3), tr td:nth-child(3) {
            display:none;
        }
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

    .droparea {
        padding: 100px 30px 100px 30px;
        border: dotted grey 2px;
        border-radius: 10px;
    }

    .loader {
        display: inline-block;
        border: 2px solid #f3f3f3;
        border-radius: 50%;
        border-top: 2px solid #3d3d3d;
        width: 10px;
        height: 10px;
        animation: spin 0.5s linear infinite;
    }

    @keyframes spin {
        0% { transform: rotate(0deg); }
        100% { transform: rotate(360deg); }
    }

</style>