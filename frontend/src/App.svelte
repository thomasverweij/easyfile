<script>
	import CreateBucket from "./CreateBucket.svelte";
	import Bucket from "./Bucket.svelte"
	import BucketLogin from "./BucketLogin.svelte"
	import { onMount } from 'svelte';
	import { getBucket, getToken, createBucket } from './api.js';
	import { messageStore, tokenStore } from './store.js';
	import Notification from "./Notification.svelte";
	import { notify } from "./utils";

	let bucketId = window.location.hash.split('#')[1] || undefined;

	tokenStore.subscribe(value => {
			localStorage.setItem("token", value);
		});

</script>
<main>
	<h2 class="title"><a href="/">EasyFile</a></h2>
	{#if !bucketId && !$tokenStore}
		<CreateBucket bind:bucketId={bucketId} />
	{:else if !$tokenStore}
		<BucketLogin bind:bucketId={bucketId} />
	{:else}
		<Bucket bucketId={bucketId} />
	{/if}
	<Notification />

</main>

<style>
	:global(body) {
		/* background-color: rgb(233, 233, 233); */
		color:rgb(97, 97, 97);
		padding: 30px;
	}

	:global(input) {
		background-color:  rgb(233, 233, 233);
		border: none;
	}

	:global(button) {
		background-color:  rgb(233, 233, 233);
		border: none;
	}

	:global(a), :global(a:visited) {	
		color: rgb(107, 107, 107);
	}

	h2 {
		font-family: Didot;
		font-size: 40pt;
		text-align: center;
		color: rgb(77, 77, 77);
	}

	@media (max-width: 500px) {
		h2 {
			margin: 10px;
		}
	}


	a {
  		color:inherit;
  		text-decoration: none;
	}
	 
	main {
		max-width: 650px;
		margin: 0 auto;
		padding-bottom: 30px;
	}


	@media (max-width: 700px) {
		main {
			max-width: none;
		}
	}
</style>