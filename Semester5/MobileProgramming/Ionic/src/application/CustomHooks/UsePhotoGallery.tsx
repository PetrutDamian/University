import {
  CameraPhoto,
  CameraResultType,
  CameraSource,
  FilesystemDirectory,
} from "@capacitor/core";
import { useCamera } from "@ionic/react-hooks/camera";
import { base64FromPath, useFilesystem } from "@ionic/react-hooks/filesystem";
export interface Photo {
  filepath: string;
  webviewPath: string;
}

export function usePhotoGallery() {
  const { getPhoto } = useCamera();
  const takePhoto = async () => {
    const cameraPhoto = await getPhoto({
      resultType: CameraResultType.Uri,
      source: CameraSource.Camera,
      quality: 100,
    });
    const fileName = new Date().getTime() + ".jpeg";
    const savedFileImage = await savePicture(cameraPhoto, fileName);
    return savedFileImage;
  };
  const { deleteFile, getUri, readFile, writeFile } = useFilesystem();
  const savePicture = async (
    photo: CameraPhoto,
    fileName: string
  ): Promise<Photo> => {
    const base64Data = await base64FromPath(photo.webPath!);
    await writeFile({
      path: fileName,
      data: base64Data,
      directory: FilesystemDirectory.Data,
    });

    return {
      filepath: fileName,
      webviewPath: photo.webPath || "",
    };
  };
  return {
    takePhoto,
    writePhoto: async (path: string, data: string) => {
      writeFile({
        path: path,
        data: data,
        directory: FilesystemDirectory.Data,
      });
    },
    readPhoto: async (path: string) => {
      const file = await readFile({
        path: path,
        directory: FilesystemDirectory.Data,
      });
      return file.data;
    },
    deletePhoto: async (path: string) => {
      console.log(path);
      const filename = path.substr(path.lastIndexOf("/") + 1);
      deleteFile({
        path: filename,
        directory: FilesystemDirectory.Data,
      });
    },
  };
}
