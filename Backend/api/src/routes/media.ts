import { Router, Request, Response } from 'express';
import Rabbitmq from '../../../common/rabbitmq';
import { respond } from '..';
import { unpackParams } from '../middlewares/unpack';

const router = Router();

export default () => {
	/**
	 * @swagger
	 * /media?set={set}:
	 *   get:
	 *     tags:
	 *       - Media
	 *     summary: Get a set of 10 media
	 *     description: Get a set of 10 media, ordered by newest created. Set = 1 gets the fist 10 media, set = 2 gets the next 10, etc.
	 *     parameters:
	 *       - in: path
	 *         name: set
	 *         schema:
	 *           type: string
	 *           required: true
	 *     responses:
	 *       200:
	 *         description: Success
	 *         content:
	 *           application/json:
	 *             schema:
	 *               $ref: '#/components/schemas/MediaArray'
	 *       500:
	 *         description: Internal Server Error
	 */
	router.get('/', unpackParams, async (req: Request, res: Response) => {
		const data = req.body;
		const result = await Rabbitmq.sendRPC(
			'media.get_all',
			JSON.stringify(data)
		);
		respond(res, result);
	});

	/**
	 * @swagger
	 * /media/{id}:
	 *   get:
	 *     tags:
	 *       - Media
	 *     summary: Get one media
	 *     description: Get the media of the given id
	 *     parameters:
	 *       - in: path
	 *         name: id
	 *         schema:
	 *           type: string
	 *           required: true
	 *     responses:
	 *       200:
	 *         description: Success
	 *         content:
	 *           application/json:
	 *             schema:
	 *               $ref: '#/components/schemas/Media'
	 *       500:
	 *         description: Internal Server Error
	 */
	router.get('/:id', async (req: Request, res: Response) => {
		const data = { ...req.body, ...req?.params };
		const result = await Rabbitmq.sendRPC(
			'media.get_one',
			JSON.stringify(data)
		);
		respond(res, result);
	});

	/**
	 * @swagger
	 * /media:
	 *   post:
	 *     tags:
	 *       - Media
	 *     summary: Create a new media
	 *     description: Create a new media with the given data
	 *     requestBody:
	 *         content:
	 *            application/x-www-form-urlencoded:
	 *              schema:
	 *                 $ref: '#/components/schemas/MediaCreate'
	 *            application/json:
	 *              schema:
	 *                 $ref: '#/components/schemas/MediaCreate'
	 *     responses:
	 *       200:
	 *         description: Success
	 *       500:
	 *         description: Internal Server Error
	 */
	router.post('/', async (req: Request, res: Response) => {
		const data = req.body;
		const result = await Rabbitmq.sendRPC(
			'media.post',
			JSON.stringify(data)
		);
		respond(res, result);
	});

	/**
	 * @swagger
	 * /media/{id}:
	 *   patch:
	 *     tags:
	 *       - Media
	 *     summary: Update one media
	 *     description: Update the media of the given id with the given data
	 *     parameters:
	 *       - in: path
	 *         name: id
	 *         schema:
	 *           type: string
	 *           required: true
	 *     requestBody:
	 *         content:
	 *            application/x-www-form-urlencoded:
	 *              schema:
	 *                 $ref: '#/components/schemas/MediaUpdate'
	 *            application/json:
	 *              schema:
	 *                 $ref: '#/components/schemas/MediaUpdate'
	 *     responses:
	 *       200:
	 *         description: Success
	 *       500:
	 *         description: Internal Server Error
	 */
	router.patch('/:id', async (req: Request, res: Response) => {
		const data = { ...req.body, ...req?.params };
		const result = await Rabbitmq.sendRPC(
			'media.patch',
			JSON.stringify(data)
		);
		respond(res, result);
	});

	return router;
};
