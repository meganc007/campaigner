export default function Input({
  labelText,
  inputId,
  inputType,
  placeholder,
  errorMessage,
}: {
  labelText: string;
  inputId: string;
  inputType: string;
  placeholder: string;
  errorMessage?: string;
}) {
  return (
    <div>
      <label htmlFor={inputId}>{labelText}</label>
      <input
        id={inputId}
        className="w-11/12 p-2 skew-x-[-12deg] border-y-4 border-x-8 border-gunmetal bg-transparent placeholder:text-zinc-600 placeholder:tracking-wider focus:outline-none invalid:border-red-600 invalid:bg-red-300 md:w-full"
        type={inputType}
        placeholder={placeholder}
      />
      {errorMessage && <p className="text-red-600 text-sm mt-0">Wrong!</p>}
    </div>
  );
}
