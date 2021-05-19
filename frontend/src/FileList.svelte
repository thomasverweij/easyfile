<script>        
    import { notify, timeSince } from './utils.js'
    import { deleteFile, downloadFile } from './api.js';
    import { tokenStore } from './store.js'


    export let bucketData;
    export let uploading;

    let currentFileMenu;

    let download = async (e) => {
        e.preventDefault();
        let id = e.currentTarget.dataset.id;
        let filename = e.currentTarget.dataset.filename;
        let blob = await downloadFile(id, $tokenStore)
            .catch(() => notify("could not download file"))
        let elemx = window.document.createElement('a');
        elemx.href = window.URL.createObjectURL(blob);
        elemx.download = filename;
        elemx.style.display = 'none';
        document.body.appendChild(elemx);
        elemx.click();
        document.body.removeChild(elemx);
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
<table class:invisible={hideFilelist}> 
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
                <a href="/" data-id="{file.id}" data-filename="{file.fileName}" on:click|preventDefault={download}>Download</a>
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
<p class:invisible={!hideFilelist} class="droparea">
    <i>Drag and drop your file here, or click 'Add file' to upload your file.</i>
</p>
</div>
<style>
    #fileList {
        padding-top: 50px;
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

    .droparea {
        padding: 100px 50px 100px 50px;
        border: dotted grey 2px;
        border-radius: 10px;
    }

    .loader {
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