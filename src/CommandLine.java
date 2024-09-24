import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class CommandLine {
    private String currentPath = System.getProperty("user.home"); // текущая папка (по умолчанию равна домашней директории пользователя)
    //private String currentPath = "C:\\Users\\Татьяна Гриценко\\Desktop\\myNewDir";
    private boolean IsExit = false;

    public void start() {
        while (!IsExit)
            show();

        System.out.println("Выход из командной строки");
    }

    private void show() {
        System.out.print(currentPath + "> "); // выводим текущую директорию

        Scanner in = new Scanner(System.in);
        String command = in.nextLine(); // получаем команду от пользователя

        execute(command); // выполняем команду
    }

    private void execute(String command) {
        if (command != null) {
            //System.out.println("Получена команда: " + command);

            int whiteSpace = command.indexOf(' '); // находим пробел, который разделяет команду и параметры команды

            String cmd, parameters;
            if (whiteSpace != -1) {
                cmd = command.substring(0,whiteSpace);
                parameters = command.substring(whiteSpace+1);
            }
            else {
                cmd = command;
                parameters = null;
            }

            switch (cmd) {
                case "cd":
                    changeDir(parameters);
                    break;
                case "ls":
                    showList();
                    break;
                case "mkdir":
                    makeDir(parameters);
                    break;
                case "rmdir":
                    remove(parameters);
                    break;
                case "mkfile":
                    makeFile(parameters);
                    break;
                case "rmfile":
                    remove(parameters);
                    break;
                case "print":
                    printFile(parameters);
                    break;
                case "write":
                    writeFile(parameters);
                    break;
                case "exit":
                    IsExit = true;
                    break;
                default:
                    System.out.println("Команда не определена");
            }
        }
    }

    private void changeDir(String parameters) {
        if (parameters != null) {
            File dir = new File(parameters);
            if (dir.exists() && dir.isDirectory()) { // если нам дали абсолютный путь
                currentPath = dir.getAbsolutePath();
            }
            else {
                String absPath = currentPath + "\\" + parameters; // если нам дали относительный путь
                File absDir = new File(absPath);
                if (absDir.exists() && absDir.isDirectory())
                    currentPath = absDir.getAbsolutePath();
                else
                    System.out.println("Данной директории не существует");
            }
        }
        else
            System.out.println("Не указан путь к каталогу");
    }

    private void showList() {
        File dir = new File(currentPath);
        File[] list = dir.listFiles();

        if (list != null)
            for (File item : list) {
                if (item.isDirectory())
                    System.out.printf("%s \t | каталог \n", item.getName());
                else
                    System.out.printf("%s \t | файл \n", item.getName());
            }
    }

    private void makeDir(String parameters) {
        if (parameters != null) {
            File newDir = new File(currentPath + "\\" + parameters);
            if (!newDir.exists()) {
                if (!newDir.mkdir())
                    System.out.println("Не удалось создать каталог");
            }
            else
                System.out.println("Данный каталог уже существует");
        }
        else
            System.out.println("Не указано название каталога");

    }

    private void makeFile(String parameters) {
        if (parameters != null) {
            File newFile = new File(currentPath, parameters);
            try
            {
                boolean created = newFile.createNewFile();
                if (!created)
                    System.out.println("Не удалось создать файл");
            }
            catch(IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        else
            System.out.println("Не указано имя нового файла");
    }

    private void remove(String parameters) {
        if (parameters != null) {
            File dir = new File(currentPath + "\\" + parameters);
            if (dir.exists()) {
                if (!dir.delete())
                    System.out.println("Не удалось удалить каталог/файл");
            }
            else
                System.out.println("Данный каталог/файл не существует");
        }
        else
            System.out.println("Не указано название каталога/файла");
    }

    private void writeFile(String parameters) {
        if (parameters != null) {
            File file = new File(currentPath, parameters);
            if (file.exists()) {
                if (file.canWrite()) {
                    try (FileOutputStream fos = new FileOutputStream(file.getAbsolutePath())) {
                        System.out.print("Введите новое содержимое файла: ");
                        Scanner in = new Scanner(System.in);
                        String text = in.nextLine();

                        byte[] buffer = text.getBytes();

                        fos.write(buffer);
                    }
                    catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                else
                    System.out.println("Файл нельзя изменить");
            }
            else
                System.out.println("Данный файл не существует");
        }
        else
            System.out.println("Не указано название файла");

    }

    private void printFile(String parameters) {
        if (parameters != null) {
            File file = new File(currentPath, parameters);
            if (file.exists()) {
                if (file.canRead()) {
                    try (FileInputStream fin = new FileInputStream(file.getAbsolutePath())) {
                        byte[] buffer = new byte[256];

                        int count;
                        while ((count=fin.read(buffer)) != -1) {
                            for (int i = 0; i < count; i++)
                                System.out.print((char)buffer[i]);

                            System.out.println();
                        }
                    }
                    catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                else
                    System.out.println("Файл нельзя прочитать");
            }
            else
                System.out.println("Данный файл не существует");
        }
        else
            System.out.println("Не указано название файла");
    }

}
