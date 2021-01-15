<script>
	import CreateBucket from "./CreateBucket.svelte";
	import Bucket from "./Bucket.svelte"
	import BucketLogin from "./BucketLogin.svelte"
	import { onMount } from 'svelte';
	import { getBucket, getToken, createBucket } from './api.js';

	let token;
	let bucketId = window.location.hash.split('#')[1] || null;


</script>
<main>
	<h2><a href="/">Easyfile</a> 🔐</h2>
	{#if !bucketId && !token}
		<CreateBucket bind:bucketId={bucketId} bind:token={token} />
	{:else if !token}
		<BucketLogin bind:bucketId={bucketId} bind:token={token} />
	{:else}
		<Bucket bucketId={bucketId} token={token} />
	{/if}
</main>

<style>
	main {
		text-align: center;
		padding: 1em;
		max-width: 600px;
		margin: 0 auto;
	}

	a {
		color: black;
	}

	@media (max-width: 640px) {
		main {
			max-width: none;
		}
	}
</style>