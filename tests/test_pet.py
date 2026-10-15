import unittest
from pet import Motion, WIDTH, HEIGHT
from character import draw


class Canvas:
    def __init__(self):
        self.rectangles = []
        self.labels = []
    def delete(self, tag):
        self.rectangles.clear()
        self.labels.clear()
    def create_rectangle(self, *args, **kwargs):
        self.rectangles.append(args)
    def create_text(self, *args, **kwargs):
        self.labels.append(kwargs['text'])


class PetTests(unittest.TestCase):
    def test_bounces_at_both_edges(self):
        pet = Motion(799 - WIDTH, 400)
        pet.step(1, (0, 0, 800, 600))
        self.assertEqual(pet.x, 800 - WIDTH)
        self.assertEqual(pet.direction, -1)
        pet.x = 1
        pet.step(1, (0, 0, 800, 600))
        self.assertEqual(pet.x, 0)
        self.assertEqual(pet.direction, 1)

    def test_drag_clamp_respects_taskbar(self):
        pet = Motion(-500, 2000)
        pet.clamp((0, 0, 1920, 1040))
        self.assertEqual((pet.x, pet.y), (0, 1040 - HEIGHT))

    def test_animation_stays_inside_window(self):
        canvas = Canvas()
        for direction in (-1, 1):
            for state in ('idle', 'walk', 'happy', 'sleep', 'drag'):
                draw(canvas, direction, 1, state)
                self.assertGreater(len(canvas.rectangles), 100)
                for x1, y1, x2, y2 in canvas.rectangles:
                    self.assertTrue(0 <= x1 < x2 <= WIDTH)
                    self.assertTrue(0 <= y1 < y2 <= HEIGHT)
                if state == 'happy':
                    self.assertIn('♥ 喵～', canvas.labels)


if __name__ == '__main__':
    unittest.main()
